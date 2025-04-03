### URL
http://localhost:9092
---
### ER
* products - продукты
* images - изображения 
* orders - заказы 
* items - товары в заказе

---
#### БД для прода: 
###### postgresql://postgres:postgres@localhost:5433/postgres
---
#### БД для теста: 
###### postgresql://postgres:postgres@localhost:5433/test
---
#### Как запускать:
##### Локально
##### .\gradlew clean build bootJar  
##### cd .\build\libs\  
##### java -jar .\shop-0.0.1-SNAPSHOT.jar 
