FROM maven:3.9.5-openjdk-22 AS build
COPY . .
RUN maven clean package - DskipTests

FROM openjdk:22-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080

ENTRYPOINT ["java", "jar", "demo.jar"]