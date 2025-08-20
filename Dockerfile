# Use lightweight OpenJDK 17 image
FROM openjdk:21-jdk-alpine

# Create a volume for temporary files
VOLUME /tmp

# ARG to specify the jar file name (default to target/*.jar)
ARG JAR_FILE=target/*.jar

# Copy the jar file into the container as app.jar
COPY ${JAR_FILE} app.jar

# Run the jar file when container starts
ENTRYPOINT ["java","-jar","/app.jar"]
