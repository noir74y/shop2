FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/shop2-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9092
CMD ["java", "-jar", "app.jar"]