#!/bin/bash
set -e  # Exit on error

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

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DUMP_FILE="$SCRIPT_DIR/remote_dump_$TIMESTAMP.dump"
BACKUP_FILE="$SCRIPT_DIR/local_backup_$TIMESTAMP.dump"
LOG_FILE="$SCRIPT_DIR/migration_$TIMESTAMP.log"

echo "=== Database Migration Started ===" | tee -a "$LOG_FILE"
echo "Timestamp: $TIMESTAMP" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 1: Verify PostgreSQL tools are installed
echo "[1/8] Verifying PostgreSQL client tools..." | tee -a "$LOG_FILE"
if ! command -v pg_dump &> /dev/null; then
    echo "ERROR: pg_dump not found at $PG_BIN" | tee -a "$LOG_FILE"
    echo "Please ensure Postgres.app is installed at /Applications/Postgres.app" | tee -a "$LOG_FILE"
    exit 1
fi
echo "✓ PostgreSQL client tools found: $(pg_dump --version | head -1)" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 2: Test remote connection
echo "[2/8] Testing remote database connection..." | tee -a "$LOG_FILE"
if ! PGPASSWORD="$REMOTE_PASSWORD" psql -h "$REMOTE_HOST" -p "$REMOTE_PORT" -U "$REMOTE_USER" -d "$REMOTE_DB" -c "SELECT 1" >> "$LOG_FILE" 2>&1; then
    echo "ERROR: Cannot connect to remote database" | tee -a "$LOG_FILE"
    echo "Host: $REMOTE_HOST:$REMOTE_PORT" | tee -a "$LOG_FILE"
    echo "Database: $REMOTE_DB" | tee -a "$LOG_FILE"
    echo "User: $REMOTE_USER" | tee -a "$LOG_FILE"
    echo "Check the log file for details: $LOG_FILE" | tee -a "$LOG_FILE"
    exit 1
fi
echo "✓ Remote database accessible" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 3: Test local connection
echo "[3/8] Testing local database connection..." | tee -a "$LOG_FILE"
if ! PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "postgres" -c "SELECT 1" >> "$LOG_FILE" 2>&1; then
    echo "ERROR: Cannot connect to local PostgreSQL" | tee -a "$LOG_FILE"
    echo "Host: $LOCAL_HOST:$LOCAL_PORT" | tee -a "$LOG_FILE"
    echo "User: $LOCAL_USER" | tee -a "$LOG_FILE"
    echo "Make sure PostgreSQL is running locally" | tee -a "$LOG_FILE"
    echo "Check the log file for details: $LOG_FILE" | tee -a "$LOG_FILE"
    exit 1
fi
echo "✓ Local PostgreSQL accessible" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 4: Backup local database (if exists)
echo "[4/8] Backing up local database (if exists)..." | tee -a "$LOG_FILE"
if PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -lqt | cut -d \| -f 1 | grep -qw "$LOCAL_DB"; then
    echo "Local database exists, creating backup..." | tee -a "$LOG_FILE"
    PGPASSWORD="$LOCAL_PASSWORD" pg_dump -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -F c -f "$BACKUP_FILE" >> "$LOG_FILE" 2>&1
    echo "✓ Backup created: $BACKUP_FILE" | tee -a "$LOG_FILE"
else
    echo "No existing local database to backup" | tee -a "$LOG_FILE"
fi
echo "" | tee -a "$LOG_FILE"

# Step 5: Export remote database
echo "[5/8] Exporting remote database..." | tee -a "$LOG_FILE"
echo "This may take several minutes depending on database size..." | tee -a "$LOG_FILE"
PGPASSWORD="$REMOTE_PASSWORD" pg_dump \
    -h "$REMOTE_HOST" \
    -p "$REMOTE_PORT" \
    -U "$REMOTE_USER" \
    -d "$REMOTE_DB" \
    -F c \
    -f "$DUMP_FILE" >> "$LOG_FILE" 2>&1
echo "✓ Remote database exported: $DUMP_FILE" | tee -a "$LOG_FILE"
DUMP_SIZE=$(du -h "$DUMP_FILE" | cut -f1)
echo "  Dump file size: $DUMP_SIZE" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 6: Drop and recreate local database
echo "[6/8] Recreating local database..." | tee -a "$LOG_FILE"
# Terminate existing connections to the database
echo "Terminating existing connections to $LOCAL_DB..." | tee -a "$LOG_FILE"
PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "postgres" -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$LOCAL_DB' AND pid <> pg_backend_pid();" >> "$LOG_FILE" 2>&1
# Drop and recreate database
PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "postgres" -c "DROP DATABASE IF EXISTS $LOCAL_DB;" >> "$LOG_FILE" 2>&1
PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "postgres" -c "CREATE DATABASE $LOCAL_DB;" >> "$LOG_FILE" 2>&1
echo "✓ Local database recreated" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 7: Restore to local database
echo "[7/8] Restoring to local database..." | tee -a "$LOG_FILE"
echo "This may take several minutes depending on database size..." | tee -a "$LOG_FILE"
PGPASSWORD="$LOCAL_PASSWORD" pg_restore \
    -h "$LOCAL_HOST" \
    -p "$LOCAL_PORT" \
    -U "$LOCAL_USER" \
    -d "$LOCAL_DB" \
    --no-owner \
    --no-privileges \
    -v \
    "$DUMP_FILE" >> "$LOG_FILE" 2>&1
echo "✓ Database restored" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Step 8: Verify migration
echo "[8/8] Verifying migration..." | tee -a "$LOG_FILE"
TABLES=("player" "ladder" "player_ladder" "court" "match" "match_result" "set_score")
ALL_MATCH=true

for table in "${TABLES[@]}"; do
    REMOTE_COUNT=$(PGPASSWORD="$REMOTE_PASSWORD" psql -h "$REMOTE_HOST" -p "$REMOTE_PORT" -U "$REMOTE_USER" -d "$REMOTE_DB" -t -c "SELECT COUNT(*) FROM $table;" 2>> "$LOG_FILE")
    LOCAL_COUNT=$(PGPASSWORD="$LOCAL_PASSWORD" psql -h "$LOCAL_HOST" -p "$LOCAL_PORT" -U "$LOCAL_USER" -d "$LOCAL_DB" -t -c "SELECT COUNT(*) FROM $table;" 2>> "$LOG_FILE")

    REMOTE_COUNT=$(echo $REMOTE_COUNT | xargs)
    LOCAL_COUNT=$(echo $LOCAL_COUNT | xargs)

    if [ "$REMOTE_COUNT" == "$LOCAL_COUNT" ]; then
        echo "  ✓ $table: $LOCAL_COUNT rows" | tee -a "$LOG_FILE"
    else
        echo "  ✗ $table: Remote=$REMOTE_COUNT, Local=$LOCAL_COUNT (MISMATCH)" | tee -a "$LOG_FILE"
        ALL_MATCH=false
    fi
done

echo "" | tee -a "$LOG_FILE"

if [ "$ALL_MATCH" = true ]; then
    echo "=== Migration Completed Successfully ===" | tee -a "$LOG_FILE"
    echo "" | tee -a "$LOG_FILE"
    echo "Files created:" | tee -a "$LOG_FILE"
    echo "  - Dump: $DUMP_FILE" | tee -a "$LOG_FILE"
    if [ -f "$BACKUP_FILE" ]; then
        echo "  - Backup: $BACKUP_FILE" | tee -a "$LOG_FILE"
    fi
    echo "  - Log: $LOG_FILE" | tee -a "$LOG_FILE"
    echo "" | tee -a "$LOG_FILE"
    echo "Next steps:" | tee -a "$LOG_FILE"
    echo "1. Run ./verify_migration.sh for additional verification" | tee -a "$LOG_FILE"
    echo "2. Test your application: cd ../../../.. && ./mvnw spring-boot:run" | tee -a "$LOG_FILE"
    exit 0
else
    echo "=== Migration Completed with Warnings ===" | tee -a "$LOG_FILE"
    echo "Some table counts do not match. Review log file: $LOG_FILE" | tee -a "$LOG_FILE"
    exit 1
fi
