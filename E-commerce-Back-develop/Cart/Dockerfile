FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/Cart-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8084

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]