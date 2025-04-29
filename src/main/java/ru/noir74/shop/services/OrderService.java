package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;

public interface OrderService {
    Flux<Order> findAll();

    Mono<Order> findById(Long id);

    Mono<Void> create(Flux<Item> items);

    Mono<Integer> getTotal();
}
