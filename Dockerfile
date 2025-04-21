FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/httpbin-0.9.0.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]