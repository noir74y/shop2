FROM openjdk:21-jdk-slim
WORKDIR /shop
COPY build/libs/module_shop-1.0-SNAPSHOT.jar shop.jar
CMD ["java", "-jar", "shop.jar"]