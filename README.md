### URL

http://localhost:9092
---

### ER

* products - продукты
* images - изображения
* orders - заказы
* items - товары в заказе

---
![image](https://github.com/user-attachments/assets/0fb4378d-68cf-4271-b59f-5adeeea1925a)

---

#### БД для прода:

###### postgresql://postgres:postgres@localhost:5433/postgres
---

#### БД для теста:

###### postgresql://postgres:postgres@localhost:5433/test
---

#### Как запускать:

##### Локально

###### .\gradlew clean build bootJar

###### cd .\build\libs\

###### java -jar .\shop-0.0.1-SNAPSHOT.jar

##### Докер

###### docker compose up
