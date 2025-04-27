package ru.noir74.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.entity.ProductEntity;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
        var entity = ProductMapperInterface.INSTANCE.<Product,ProductEntity>i2o(new Product());
        var mono = ProductMapperInterface.INSTANCE.mono2mono(Mono.just(new Product()));

    }

}
