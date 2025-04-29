-- Export MATCH table data to a file
-- Run this on the source server

-- First, create a temporary table to hold the data
CREATE TEMPORARY TABLE temp_match_export AS
SELECT 
    MATCH_ID,
    LADDER_ID,
    PLAYER1_ID,
    PLAYER2_ID,
    PLAYER3_ID,
    PLAYER4_ID,
    MATCH_DATE,
    COURT_ID,
    MATCH_TYPE,
    MATCH_SCHEDULED_STATUS
FROM MATCH;

-- Export the data to a file
-- Note: The file path should be accessible to the database server
-- You may need to adjust the file path based on your server configuration
COPY temp_match_export TO '/tmp/match_data.csv' WITH CSV HEADER;

-- Clean up
DROP TABLE temp_match_export; 