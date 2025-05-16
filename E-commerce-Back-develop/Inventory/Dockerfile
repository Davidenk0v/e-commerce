FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/Inventory-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8083

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]