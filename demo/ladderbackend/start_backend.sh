#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | xargs)
fi

# Set default values for environment variables if not set
export SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-"jdbc:postgresql://host.docker.internal:5432/demo"}
export SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-"postgres"}
export SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-"postgres"}
export HIBERNATE_DDL_AUTO=${HIBERNATE_DDL_AUTO:-"update"}
export SHOW_SQL=${SHOW_SQL:-"true"}
export HIBERNATE_GENERATE_STATISTICS=${HIBERNATE_GENERATE_STATISTICS:-"false"}
export HIKARI_MAX_POOL_SIZE=${HIKARI_MAX_POOL_SIZE:-"10"}
export HIKARI_MIN_IDLE=${HIKARI_MIN_IDLE:-"5"}
export HIKARI_IDLE_TIMEOUT=${HIKARI_IDLE_TIMEOUT:-"300000"}
export HIKARI_CONNECTION_TIMEOUT=${HIKARI_CONNECTION_TIMEOUT:-"20000"}
export HIKARI_MAX_LIFETIME=${HIKARI_MAX_LIFETIME:-"1200000"}
export CORS_ALLOWED_ORIGINS=${CORS_ALLOWED_ORIGINS:-"http://localhost,http://localhost:80,http://localhost:4200,https://ladder-app-a4ra.onrender.com,https://ladder-frontend-vj79.onrender.com,http://localhost:8081,https://localhost:8081,http://ladder-frontend-vj79.onrender.com,http://ladder-app-a4ra.onrender.com,https://dmvtennisladders.com"}
export CORS_ALLOWED_METHODS=${CORS_ALLOWED_METHODS:-"GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD"}
export CORS_ALLOWED_HEADERS=${CORS_ALLOWED_HEADERS:-"Authorization,Content-Type,X-Requested-With,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Access-Control-Allow-Origin,Access-Control-Allow-Methods,Access-Control-Allow-Headers,Access-Control-Allow-Credentials,Access-Control-Max-Age,X-Forwarded-For,X-Forwarded-Proto,X-Forwarded-Host"}
export CORS_ALLOW_CREDENTIALS=${CORS_ALLOW_CREDENTIALS:-"true"}
export CORS_MAX_AGE=${CORS_MAX_AGE:-"3600"}
export SSL_ENABLED=${SSL_ENABLED:-"false"}
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-"prod"}

# Verify keystore exists
#if [ ! -f "$SSL_KEY_STORE" ]; then
#    echo "Error: Keystore file not found at $SSL_KEY_STORE"
#    exit 1
#fi

# Print environment variables for debugging
echo "Environment variables(start_backend.sh):"
echo "SPRING_DATASOURCE_URL: $SPRING_DATASOURCE_URL"
echo "SPRING_DATASOURCE_USERNAME: $SPRING_DATASOURCE_USERNAME"
echo "HIBERNATE_DDL_AUTO: $HIBERNATE_DDL_AUTO"
echo "SHOW_SQL: $SHOW_SQL"
echo "HIBERNATE_GENERATE_STATISTICS: $HIBERNATE_GENERATE_STATISTICS"
echo "HIKARI_MAX_POOL_SIZE: $HIKARI_MAX_POOL_SIZE"
echo "HIKARI_MIN_IDLE: $HIKARI_MIN_IDLE"
echo "HIKARI_IDLE_TIMEOUT: $HIKARI_IDLE_TIMEOUT"
echo "HIKARI_CONNECTION_TIMEOUT: $HIKARI_CONNECTION_TIMEOUT"
echo "HIKARI_MAX_LIFETIME: $HIKARI_MAX_LIFETIME"
echo "CORS_ALLOWED_ORIGINS: $CORS_ALLOWED_ORIGINS"
echo "CORS_ALLOWED_METHODS: $CORS_ALLOWED_METHODS"
echo "CORS_ALLOWED_HEADERS: $CORS_ALLOWED_HEADERS"
echo "CORS_ALLOW_CREDENTIALS: $CORS_ALLOW_CREDENTIALS"
echo "CORS_MAX_AGE: $CORS_MAX_AGE"
echo "SSL_ENABLED: $SSL_ENABLED"
echo "PORT: $PORT"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"

# Start the Spring Boot application
echo "Starting Spring Boot application..."
java -jar app.war --spring.profiles.active=$SPRING_PROFILES_ACTIVE
