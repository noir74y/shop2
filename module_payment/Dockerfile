FROM openjdk:21-jdk-slim
WORKDIR /payment
COPY build/libs/module_payment-1.0-SNAPSHOT.jar payment.jar
CMD ["java", "-jar", "payment.jar"]