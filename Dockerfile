FROM openjdk:19-alpine
COPY build/libs/*.jar trip-advisor.jar
EXPOSE 28853
ENTRYPOINT ["java", "-jar", "/trip-advisor.jar"]