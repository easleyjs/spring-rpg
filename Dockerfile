# ---- Build ----
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests


# ---- Runtime ----
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]