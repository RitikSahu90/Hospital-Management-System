# Stage 1: Build
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
