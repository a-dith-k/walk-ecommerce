FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/walk-0.0.1-SNAPSHOT.jar .
EXPOSE 2021
ENTRYPOINT ["java","-jar","walk-0.0.1-SNAPSHOT.jar"]