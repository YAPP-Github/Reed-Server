# Build stage
FROM gradle:8.7-jdk21 AS build
ARG MODULE=apis
WORKDIR /app
COPY . .
RUN ./gradlew :${MODULE}:bootJar --no-daemon

# Run stage
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
