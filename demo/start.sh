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

# Start backend
echo -e "${GREEN}Starting backend application...${NC}"
cd ladderbackend
mvn spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
echo -e "${GREEN}Waiting for backend to start...${NC}"
sleep 15

# Start frontend
echo -e "${GREEN}Starting frontend application...${NC}"
cd ../ladderfrontend
mvn clean install -DskipTests
npm start --prefix ../ladderfrontend &
FRONTEND_PID=$!
echo -e "${GREEN}Frontend PID: ${FRONTEND_PID}${NC}"
lsof -i :4200 >> start.log
# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
