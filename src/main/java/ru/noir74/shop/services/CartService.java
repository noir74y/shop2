package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;

import java.util.List;

public interface CartService {
    List<Item> findAll();

    Integer getQuantityOfProduct(Long id);

    void addToCart(Long productId);

    void addToCart(Long productId, Integer quantity);

    void removeFromCart(Long productId);

    void setQuantity(Long productId, Integer quantity);

    Integer getTotal();

    void makeOrder();

    boolean ifProductInCart(Long productId);
}