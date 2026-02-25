#!/bin/bash

# PostgreSQL tools path (Postgres.app)
PG_BIN="/Applications/Postgres.app/Contents/Versions/18/bin"
export PATH="$PG_BIN:$PATH"

# Configuration
REMOTE_HOST="dpg-d08foundiees739ajoc0-a.ohio-postgres.render.com"
REMOTE_PORT="5432"
REMOTE_USER="craig"
REMOTE_PASSWORD="ZSel9P9YQPyQDc4Cp7b2SqTcrHWC2y2X"
REMOTE_DB="ladder"

LOCAL_HOST="localhost"
LOCAL_PORT="5432"
LOCAL_USER="postgres"
LOCAL_PASSWORD="postgres"
LOCAL_DB="ladder"

echo "=== Database Migration Verification ==="
echo ""
echo "Comparing remote and local database row counts..."
echo ""

TABLES=("player" "ladder" "player_ladder" "court" "match" "match_result" "set_score")
ALL_MATCH=true

for table in "${TABLES[@]}"; do
    echo "Verifying $table..."

    REMOTE_COUNT=$(PGPASSWORD="$REMOTE_PASSWORD" psql -h "$REMOTE_HOST" -p "$REMOTE_PORT" -U "$REMOTE_USER" -d "$REMOTE_DB" -t -c "SELECT COUNT(*) FROM $table;" 2>/dev/null)
    LOCAL_COUNT=$(PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -t -c "SELECT COUNT(*) FROM $table;" 2>/dev/null)

    REMOTE_COUNT=$(echo $REMOTE_COUNT | xargs)
    LOCAL_COUNT=$(echo $LOCAL_COUNT | xargs)

    if [ -z "$REMOTE_COUNT" ]; then
        echo "  ✗ ERROR: Could not retrieve remote count for $table"
        ALL_MATCH=false
    elif [ -z "$LOCAL_COUNT" ]; then
        echo "  ✗ ERROR: Could not retrieve local count for $table"
        ALL_MATCH=false
    elif [ "$REMOTE_COUNT" == "$LOCAL_COUNT" ]; then
        echo "  ✓ $table: $LOCAL_COUNT rows (MATCH)"
    else
        echo "  ✗ $table: Remote=$REMOTE_COUNT, Local=$LOCAL_COUNT (MISMATCH)"
        ALL_MATCH=false
    fi
    echo ""
done

echo "=== Additional Verification Checks ==="
echo ""

# Check for orphaned records
echo "Checking for orphaned records in player_ladder..."
ORPHAN_COUNT=$(PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -t -c "SELECT COUNT(*) FROM player_ladder pl WHERE NOT EXISTS (SELECT 1 FROM player p WHERE p.player_id = pl.player_id) OR NOT EXISTS (SELECT 1 FROM ladder l WHERE l.ladder_id = pl.ladder_id);" 2>/dev/null)
ORPHAN_COUNT=$(echo $ORPHAN_COUNT | xargs)
if [ "$ORPHAN_COUNT" == "0" ]; then
    echo "  ✓ No orphaned records in player_ladder"
else
    echo "  ✗ Found $ORPHAN_COUNT orphaned records in player_ladder"
    ALL_MATCH=false
fi
echo ""

echo "Checking for orphaned records in match_result..."
ORPHAN_COUNT=$(PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -t -c "SELECT COUNT(*) FROM match_result mr WHERE NOT EXISTS (SELECT 1 FROM player p WHERE p.player_id = mr.player1_id);" 2>/dev/null)
ORPHAN_COUNT=$(echo $ORPHAN_COUNT | xargs)
if [ "$ORPHAN_COUNT" == "0" ]; then
    echo "  ✓ No orphaned records in match_result"
else
    echo "  ✗ Found $ORPHAN_COUNT orphaned records in match_result"
    ALL_MATCH=false
fi
echo ""

echo "Checking for orphaned records in set_score..."
ORPHAN_COUNT=$(PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -t -c "SELECT COUNT(*) FROM set_score ss WHERE NOT EXISTS (SELECT 1 FROM match_result mr WHERE mr.match_result_id = ss.match_result_id);" 2>/dev/null)
ORPHAN_COUNT=$(echo $ORPHAN_COUNT | xargs)
if [ "$ORPHAN_COUNT" == "0" ]; then
    echo "  ✓ No orphaned records in set_score"
else
    echo "  ✗ Found $ORPHAN_COUNT orphaned records in set_score"
    ALL_MATCH=false
fi
echo ""

# Check sequences
echo "=== Sequence Verification ==="
echo ""
echo "Checking sequence values..."
PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -c "SELECT sequencename, last_value FROM pg_sequences WHERE schemaname = 'public' ORDER BY sequencename;" 2>/dev/null
echo ""

# Summary
echo "=== Verification Summary ==="
if [ "$ALL_MATCH" = true ]; then
    echo "✓ All verification checks passed!"
    echo "The migration was successful."
    exit 0
else
    echo "✗ Some verification checks failed."
    echo "Please review the output above for details."
    exit 1
fi
