-- Import MATCH table data from file
-- Run this on the target server

-- First, create a temporary table to hold the imported data
CREATE TEMPORARY TABLE temp_match_import (
    MATCH_ID INT,
    LADDER_ID INT,
    PLAYER1_ID INT,
    PLAYER2_ID INT,
    PLAYER3_ID INT,
    PLAYER4_ID INT,
    MATCH_DATE TIMESTAMP,
    COURT_ID INT,
    MATCH_TYPE VARCHAR(255),
    MATCH_SCHEDULED_STATUS VARCHAR(255)
);

-- Import the data from the file
-- Note: The file path should be accessible to the database server
-- You may need to adjust the file path based on your server configuration
COPY temp_match_import FROM '/tmp/match_data.csv' WITH CSV HEADER;

-- Insert the data into the MATCH table
-- Note: We're not copying MATCH_ID since it's an identity column
INSERT INTO MATCH (
    LADDER_ID,
    PLAYER1_ID,
    PLAYER2_ID,
    PLAYER3_ID,
    PLAYER4_ID,
    MATCH_DATE,
    COURT_ID,
    MATCH_TYPE,
    MATCH_SCHEDULED_STATUS
)
SELECT 
    LADDER_ID,
    PLAYER1_ID,
    PLAYER2_ID,
    PLAYER3_ID,
    PLAYER4_ID,
    MATCH_DATE,
    COURT_ID,
    MATCH_TYPE,
    MATCH_SCHEDULED_STATUS
FROM temp_match_import;

-- Clean up
DROP TABLE temp_match_import; 