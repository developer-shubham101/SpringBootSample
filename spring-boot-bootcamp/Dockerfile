# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-alpine
#FROM adoptopenjdk/openjdk11:alpine-jre

# Set the working directory in the container
WORKDIR /opt/workdir/

# Copy the project’s jar file into the container at /app
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 9000

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
