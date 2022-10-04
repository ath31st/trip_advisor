FROM openjdk:19-alpine
COPY build/libs/*.jar trip-advisor.jar
ENTRYPOINT ["java", "-jar", "/trip-advisor.jar"]