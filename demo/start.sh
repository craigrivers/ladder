#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to handle cleanup
cleanup() {
    echo -e "\n${RED}Shutting down applications...${NC}"
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    exit 0
}

# Set up trap for cleanup
trap cleanup SIGINT SIGTERM

# Debug: List current directory contents
echo -e "${GREEN}Current directory contents:${NC}"
ls -la

# Start nginx in the background
echo -e "${GREEN}Starting nginx...${NC}"
nginx

# Start the Spring Boot application
echo -e "${GREEN}Starting backend application...${NC}"
if [ -f "app.war" ]; then
    # Set default database connection if not provided
    export SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-"jdbc:postgresql://host.docker.internal:5432/demo"}
    export SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-"postgres"}
    export SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-"postgres"}
    
    java -jar app.war &
    BACKEND_PID=$!
else
    echo -e "${RED}Error: app.war not found in current directory${NC}"
    exit 1
fi

# Wait for both processes
wait $BACKEND_PID
