FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/library-service-*.jar library-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/library-service.jar"]