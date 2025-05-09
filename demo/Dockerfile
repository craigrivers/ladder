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

# Install nginx and openssl
RUN apt-get update && apt-get install -y \
    nginx \
    openssl \
    && rm -rf /var/lib/apt/lists/*

# Copy the built backend WAR
COPY --from=backend-builder /app/target/*.war ./app.war
RUN ls -la

# Copy the built frontend files
COPY --from=frontend-builder /app/dist/ladder-front-end/browser /app/frontend

# Copy SSL certificates and configuration
COPY ladderbackend/src/main/resources/keystore.p12 /app/keystore.p12
RUN echo "nydc11" > /app/keystore.pass
RUN chmod 600 /app/keystore.pass

# Convert PKCS12 keystore to PEM format for nginx and verify the conversion
RUN openssl pkcs12 -in /app/keystore.p12 -out /app/cert.pem -clcerts -nokeys -passin file:/app/keystore.pass && \
    openssl pkcs12 -in /app/keystore.p12 -out /app/key.pem -nocerts -nodes -passin file:/app/keystore.pass && \
    chmod 600 /app/cert.pem /app/key.pem && \
    openssl x509 -in /app/cert.pem -text -noout && \
    openssl rsa -in /app/key.pem -check -noout

# Create nginx log directory and set permissions
RUN mkdir -p /var/log/nginx && \
    chown -R www-data:www-data /var/log/nginx

# Copy nginx configuration
COPY ladderfrontend/nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 80 443 8080

# Start both applications
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# Note: When running this container, use:
# docker run -p 80:80 -p 443:443 -p 8080:8080 --add-host=host.docker.internal:host-gateway ladder-app
CMD ["/app/start.sh"] 

# Docker commands to build and run the container
#docker build -t ladder-app .
#docker run -p 80:80 -p 443:443 -p 8081:8081 --add-host=host.docker.internal:host-gateway ladder-app
# Docker commands to tag and push the image to Docker Hub
#docker tag ladder-app craigrivers/ladder-app:latest && docker push craigrivers/ladder-app:latest
#docker login   (Not always needed)
#docker push craigrivers/ladder-app:latest