package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;

public interface CartService {
    Flux<Item> findAll(String userName);

    Mono<Integer> getQuantityOfProduct(Long id, String userName);

    Mono<Void> addToCart(Long productId, String userName);

    Mono<Void> addToCart(Long productId, Integer quantity, String userName);

    Mono<Void> removeFromCart(Long productId, String userName);

    Mono<Void> setQuantity(Long productId, Integer quantity, String userName);

    Mono<Integer> getTotal(String userName);

    Mono<Void> makeOrder(String userName);

    Mono<Boolean> ifProductInCart(Long productId);
}