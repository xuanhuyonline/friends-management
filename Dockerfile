FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/friends_management-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
