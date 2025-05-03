package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.models.domain.Product;

import java.io.IOException;

public interface ProductService {
    Flux<Product> getPage(Integer page, Integer size, ProductSorting sort);

    Mono<Product> get(Long id);

    Mono<Product> create(Mono<Product> product);

    Mono<Product> update(Mono<Product> product);

    Mono<Void> delete(Long id);
}
