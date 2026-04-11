FROM eclipse-temurin:21-jdk
LABEL authors="easleyjs"

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]