package ru.noir74.shop.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;

public interface CartService {
    Flux<Item> findAll();

    Mono<Integer> getQuantityOfProduct(Long id);

    Mono<Void> addToCart(Long productId);

    Mono<Void> addToCart(Long productId, Integer quantity);

    Mono<Void> removeFromCart(Long productId);

    Mono<Void> setQuantity(Long productId, Integer quantity);

    Mono<Integer> getTotal();

    Mono<Void> makeOrder();

    Mono<Boolean> ifProductInCart(Long productId);
}