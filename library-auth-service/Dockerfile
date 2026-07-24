FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/auth-service-*.jar auth-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]