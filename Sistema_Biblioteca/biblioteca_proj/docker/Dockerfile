# Fase 1: Build dell'applicazione con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true -Dmaven.test.compile.skip=true

# Fase 2: Creazione dell'immagine finale e leggera
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Sistema_Biblioteca-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]