package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.OrderItem;

import java.util.List;

public interface CartService {
    List<OrderItem> get();

    void addToCart(Long itemId);

    void removeFromCart(Long itemId);

    void setQuantity(Long itemId, Integer quantity);

    void makeOrder();
}
