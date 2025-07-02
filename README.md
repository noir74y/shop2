### URL

http://localhost:9092/product
---

### ER

* products - продукты
* images - изображения
* orders - заказы
* items - товары в заказе

---

![Снимок2](https://github.com/user-attachments/assets/cf364c5a-b745-47e2-8908-db3e64ab9361)

---

#### БД для прода:

###### postgresql://postgres:postgres@localhost:5433/postgres
---

#### БД для теста:

###### postgresql://postgres:postgres@localhost:5433/test
---

#### запустить KeyCloack (локально или в докере)

###### залить через админку конфиг shop-realm.json
###### добавить пару пользователей
###### в application.yaml поменять клиентский секрет для: 
###### 1. spring.security.oauth2.client.registration.keycloak-user.client-secret
###### 2. spring.security.oauth2.client.registration.keycloak-payment-service.client-secret

#### Как запускать:

##### Локально

###### cd <project's root>

###### .\gradlew clean build bootJar

##### в одном терминале:

###### cd <project's root>\module_payment\build\libs\

###### java -jar .\module_payment-1.0-SNAPSHOT.jar

##### в другом терминале:

###### cd <project's root>\module_shop\build\libs\

###### java -jar .\module_shop-1.0-SNAPSHOT.jar

##### Докер

###### cd <project's root>

###### docker compose up --build

###### в Keycloack залить через админку (localhost:8081) конфиг shop-realm.json

###### добавить пару пользователей
###### в docker-compose.yaml поменять клиентский секрет для: 
###### 1. SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_USER_CLIENT_SECRET
###### 2. SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_PAYMENT_SERVICE_CLIENT_SECRET
