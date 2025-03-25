package ru.noir74.shop.repositories;

import org.springframework.stereotype.Repository;
import ru.noir74.shop.models.entity.OrderItemEntity;

import java.util.LinkedList;
import java.util.List;

@Repository
public class CartRepository {
    private final List<OrderItemEntity> cartStorage;

    public CartRepository() {
        this.cartStorage = new LinkedList<OrderItemEntity>();
    }

    public List<OrderItemEntity> findAll() {
        return cartStorage;
    }

    public void insert(OrderItemEntity orderItemEntity) {
        cartStorage.add(orderItemEntity);
    }

    public void delete(OrderItemEntity orderItemEntity) {
        cartStorage.remove(orderItemEntity);
    }

    public void replace(OrderItemEntity orderItemEntity) {
        var toBeReplaced = cartStorage.stream()
                .filter(obj -> obj.getItemEntity().getId().equals(orderItemEntity.getItemEntity().getId()))
                .findFirst().orElse(null);
        cartStorage.set(cartStorage.indexOf(toBeReplaced), orderItemEntity);
    }

}
