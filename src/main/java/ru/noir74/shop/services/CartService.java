package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;

import java.util.List;

public interface CartService {
    List<Item> findAll();

    Integer getQuantityOfProduct(Long id);

    void addToCart(Long productId);

    void removeFromCart(Long itemId);

    void setQuantity(Long itemId, Integer quantity);

    void makeOrder();
}