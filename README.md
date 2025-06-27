### URL

http://localhost:9092/product
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

###### docker compose up
