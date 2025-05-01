package ru.noir74.shop.repositories;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.entity.ItemEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class CartRepository {
    private final Map<Long, ItemEntity> cartStorage;

    public CartRepository() {
        this.cartStorage = new HashMap<>();
    }

    public Flux<ItemEntity> findAll() {
        return Flux.fromStream(cartStorage.values().stream());
    }

    public Mono<Integer> getQuantityOfProduct(Long productId) {
        return Mono.just(cartStorage.getOrDefault(productId, ItemEntity.builder().quantity(0).build()).getQuantity());
    }

    public Mono<Void> insert(ItemEntity itemEntity) {
        return Mono.just(Objects.requireNonNull(cartStorage.put(itemEntity.getProductId(), itemEntity))).then();
    }

    public Mono<Void> delete(ItemEntity itemEntity) {
        return Mono.just(Objects.requireNonNull(cartStorage.remove(itemEntity.getProductId()))).then();
    }

    public Mono<Void> replace(ItemEntity itemEntity) {
        return Mono.just(Objects.requireNonNull(cartStorage.replace(itemEntity.getProductId(), itemEntity))).then();
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(cartStorage::clear);
    }

    public Mono<Boolean> ifProductInCart(Long productId) {
        return Mono.just(cartStorage.containsKey(productId));
    }
}
