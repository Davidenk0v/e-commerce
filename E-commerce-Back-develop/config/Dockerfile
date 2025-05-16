FROM openjdk:17-jdk-slim

# Install curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/config-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8888

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
