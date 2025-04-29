# Stage 1: Build the Spring Boot backend
FROM maven:3.9-eclipse-temurin-21 AS backend-builder
WORKDIR /app
COPY ladderbackend/pom.xml .
COPY ladderbackend/src ./src
RUN mvn clean package -DskipTests
RUN ls -la target/

# Stage 2: Build the Angular frontend
FROM node:20 AS frontend-builder
WORKDIR /app
COPY ladderfrontend/package*.json ./
RUN npm install
COPY ladderfrontend/ .
RUN npm run build

# Stage 3: Final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Install nginx
RUN apt-get update && apt-get install -y \
    nginx \
    && rm -rf /var/lib/apt/lists/*

# Copy the built backend WAR
COPY --from=backend-builder /app/target/*.war ./app.war
RUN ls -la

# Copy the built frontend files
COPY --from=frontend-builder /app/dist/ladder-front-end/browser /app/frontend

# Copy nginx configuration
COPY ladderfrontend/nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 8080 80

# Start both applications
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# Note: When running this container, use:
# docker run -p 80:80 -p 8080:8080 --add-host=host.docker.internal:host-gateway ladder-app
CMD ["/app/start.sh"] 