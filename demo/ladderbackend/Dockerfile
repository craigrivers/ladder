# Build stage
FROM maven:3.9-eclipse-temurin-21 AS backend-builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY mvnw .  
RUN mvn clean package -DskipTests
RUN ls -la target/

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built WAR file
COPY --from=backend-builder /app/target/*.war ./app.war

# Create SSL directory and copy keystore
#RUN mkdir -p /app/ssl
#COPY src/main/resources/keystore.p12 /app/ssl/keystore.p12
#RUN chmod 644 /app/ssl/keystore.p12

# Copy and set up startup script
COPY start_backend.sh /app/start_backend.sh
RUN chmod +x /app/start_backend.sh

# Set environment variables
ENV SSL_ENABLED=false
ENV PORT=8081
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/ladder
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

# Expose ports
EXPOSE $PORT

# Start the application
CMD ["/app/start_backend.sh"]

# Docker commands to build and run the container
# docker build -t ladder-app . or docker build --no-cache -t ladder-app .
# docker run -p 8080:8080 -p 443:443 --add-host=host.docker.internal:host-gateway ladder-app
# Docker commands to tag and push the image to Docker Hub
#docker tag ladder-app craigrivers/ladder-app:latest
#docker login   (Not always needed)
#docker push craigrivers/ladder-app:latest
