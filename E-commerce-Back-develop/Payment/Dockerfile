FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/Payment-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8085

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]