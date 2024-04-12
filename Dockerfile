FROM maven:3.8.7-openjdk-18-slim AS build 
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/walk-0.0.1-SNAPSHOT.jar .
EXPOSE 2021
ENTRYPOINT ["java","-jar","walk-0.0.1-SNAPSHOT.jar"]
