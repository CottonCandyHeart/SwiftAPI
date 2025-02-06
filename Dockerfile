FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/SwiftAPI-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]