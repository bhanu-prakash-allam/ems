# Use the official OpenJDK 11 JRE image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file to the container
COPY target/ems-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Set the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
