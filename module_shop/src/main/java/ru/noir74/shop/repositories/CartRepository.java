package ru.noir74.shop.repositories;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.entity.ItemEntity;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepository {
    private final Map<String, Map<Long, ItemEntity>> cartStorage;

    public CartRepository() {
        this.cartStorage = new HashMap<>();
    }

    public Flux<ItemEntity> findAll(String userName) {
        return Flux.fromStream(getUserCart(userName).values().stream());
    }

    public Mono<Integer> getQuantityOfProduct(Long productId, String userName) {
        return Mono.just(getUserCart(userName).getOrDefault(productId, ItemEntity.builder().quantity(0).build()).getQuantity());
    }

    public Mono<Void> insert(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCart(userName).put(itemEntity.getProductId(), itemEntity)).then();
    }

    public Mono<Void> delete(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCart(userName).remove(itemEntity.getProductId())).then();
    }

    public Mono<Void> replace(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCart(userName).replace(itemEntity.getProductId(), itemEntity)).then();
    }

    public Mono<Void> deleteAll(String userName) {
        return Mono.fromRunnable(getUserCart(userName)::clear);
    }

    public Mono<Boolean> ifProductInAnyCart(Long productId) {
                return Mono.just(cartStorage.entrySet().stream()
                .flatMap(userCartMap -> userCartMap.getValue().entrySet().stream())
                .anyMatch(userMapEntry -> userMapEntry.getKey().equals(productId)));
    }

    private Map<Long, ItemEntity> getUserCart(String userName) {
        return cartStorage.computeIfAbsent(userName, userCart -> new HashMap<>());
    }
}
