package ru.noir74.shop.repositories;

import org.springframework.stereotype.Repository;
import ru.noir74.shop.models.entity.ItemEntity;

import java.util.LinkedList;
import java.util.List;

@Repository
public class CartRepository {
    private final List<ItemEntity> cartStorage;

    public CartRepository() {
        this.cartStorage = new LinkedList<ItemEntity>();
    }

    public List<ItemEntity> findAll() {
        return cartStorage;
    }

    public void insert(ItemEntity itemEntity) {
        cartStorage.add(itemEntity);
    }

    public void delete(ItemEntity itemEntity) {
        cartStorage.remove(itemEntity);
    }

    public void replace(ItemEntity itemEntity) {
        cartStorage.stream()
                .filter(obj -> obj.getProductEntity().getId().equals(itemEntity.getProductEntity().getId()))
                .findFirst().ifPresent(obj -> cartStorage.set(cartStorage.indexOf(obj), itemEntity));
    }

    public void deleteAll() {
        cartStorage.clear();
    }
}
