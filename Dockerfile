FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/httpbin-0.9.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]