package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.models.domain.Product;

public interface ProductService {
    Flux<Product> getPage(Integer page, Integer size, ProductSorting sort);

    Mono<Product> get(Long id);

    Mono<Void> delete(Long id);

    Mono<Product> create(Mono<Product> productMono);

    Mono<Product> update(Long id, Mono<Product> productMono);

    Mono<Product> save(Long id, Mono<Product> productMono);
}
