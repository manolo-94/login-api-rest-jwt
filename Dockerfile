# Define image that use jdk-17.
FROM eclipse-temurin:17-jdk-alpine

# Set the working diretory inside the container.
WORKDIR /app

# Copy the project jar files into the directory.
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8001

# Run the application. We can use ENTRYPOINT or CMD it's up to you.
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
