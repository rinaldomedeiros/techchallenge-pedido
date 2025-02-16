FROM openjdk:17-slim

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean bootJar --no-daemon

RUN cp build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
