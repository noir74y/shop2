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
        return Flux.fromStream(getUserCartEntries(userName).values().stream());

// типа модно, но громоздко
//        return Flux.fromStream(cartStorage
//                .entrySet()
//                .stream()
//                .filter(allUsersCartEntries -> allUsersCartEntries.getKey().equals(userName))
//                .flatMap(userCartEntries -> userCartEntries.getValue().values().stream()));
    }

    public Mono<Integer> getQuantityOfProduct(Long productId, String userName) {
        return Mono.just(getUserCartEntries(userName).getOrDefault(productId, ItemEntity.builder().quantity(0).build()).getQuantity());

// типа модно, но громоздко
//        return Mono.just(cartStorage
//                .entrySet()
//                .stream()
//                .filter(allUsersCartEntries -> allUsersCartEntries.getKey().equals(userName))
//                .flatMap(userCartEntries -> userCartEntries.getValue().entrySet().stream())
//                .filter(itemEntityEntries -> itemEntityEntries.getKey().equals(productId))
//                .findFirst()
//                .map(Map.Entry::getValue)
//                .orElse(ItemEntity.builder().quantity(0).build()).getQuantity());
    }

    public Mono<Void> insert(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCartEntries(userName).put(itemEntity.getProductId(), itemEntity)).then();
    }

    public Mono<Void> delete(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCartEntries(userName).remove(itemEntity.getProductId())).then();
    }

    public Mono<Void> replace(ItemEntity itemEntity, String userName) {
        return Mono.justOrEmpty(getUserCartEntries(userName).replace(itemEntity.getProductId(), itemEntity)).then();
    }

    public Mono<Void> deleteAll(String userName) {
        return Mono.fromRunnable(getUserCartEntries(userName)::clear);
    }

    public Mono<Boolean> ifProductInAnyCart(Long productId) {
                return Mono.just(cartStorage.entrySet().stream()
                .flatMap(userCartEntries -> userCartEntries.getValue().entrySet().stream())
                .anyMatch(itemEntityEntries -> itemEntityEntries.getKey().equals(productId)));
    }

    private Map<Long, ItemEntity> getUserCartEntries(String userName) {
        return cartStorage.getOrDefault(userName, new HashMap<>());
    }
}
