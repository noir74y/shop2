package ru.noir74.shop.repositories;

import org.springframework.stereotype.Repository;
import ru.noir74.shop.models.entity.ItemEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class CartRepository {
    private final Map<Long, ItemEntity> cartStorage;

    public CartRepository() {
        this.cartStorage = new HashMap<>();
    }

    public List<ItemEntity> findAll() {
        return cartStorage.values().stream().toList();
    }

    public Integer getQuantityOfProduct(Long productId) {
        return cartStorage.getOrDefault(productId, ItemEntity.builder().quantity(0).build()).getQuantity();
    }

    public void insert(ItemEntity itemEntity) {
        cartStorage.put(itemEntity.getProductEntity().getId(), itemEntity);
    }

    public void delete(ItemEntity itemEntity) {
        cartStorage.remove(itemEntity.getProductEntity().getId());
    }

    public void replace(ItemEntity itemEntity) {
        cartStorage.replace(itemEntity.getProductEntity().getId(), itemEntity);
    }

    public void deleteAll() {
        cartStorage.clear();
    }

    public boolean ifProductInCart(Long productId) {
        return cartStorage.containsKey(productId);
    }
}
