# GENERATE JAR FILE
FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# DOCKERFILE FOR SPRING BOOT APPLICATION
FROM eclipse-temurin:17.0.14_7-jdk-alpine-3.21
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
