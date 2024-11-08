FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/infopref-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/infopref-0.0.1-SNAPSHOT.jar"]
