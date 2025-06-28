package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;

public interface OrderService {
    Flux<Order> findAll(String username);

    Mono<Order> findById(Long id);

    Mono<Void> save(Flux<Item> items, String username);

    Mono<Integer> getTotal(String username);
}
