
# Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /springapp
COPY --from=build /target/springapp-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080