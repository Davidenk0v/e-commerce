FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/eureka-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8761

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]