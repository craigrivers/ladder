#!/bin/bash

# Stop Docker Desktop
osascript -e 'quit app "Docker Desktop"'

# Wait for Docker to fully stop
sleep 5

# Reset Docker's configuration
rm -rf ~/Library/Containers/com.docker.docker/Data/*
rm -rf ~/Library/Containers/com.docker.docker/Data/vms/*
rm -rf ~/Library/Containers/com.docker.docker/Data/com.docker.driver.amd64-linux/*

# Start Docker Desktop
open -a Docker

# Wait for Docker to start
sleep 10

# Verify Docker is running
docker info 