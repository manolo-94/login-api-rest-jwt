# Define image that use jdk-17.
# [NOTE]
#If you need the official OpenJDK distribution, go with openjdk:17-jdk-alpine.
#If you're looking for a tested, enterprise-grade distribution with added QA, eclipse-temurin:17-jdk-alpine might be a better fit.
FROM eclipse-temurin:17-jdk-alpine

# Set the working diretory inside the container.
WORKDIR /app

# Install mysql-client using apk
RUN apk update && apk add mysql-client

# Copy the project jar files into the directory.
COPY target/loginapirestjwt-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the application runs on
EXPOSE 8001

# Run the application. We can use ENTRYPOINT or CMD it's up to you.
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]
