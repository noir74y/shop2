package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;

import java.util.List;

public interface CartService {
    List<Item> get();

    void setQuantity(Long itemId, Integer quantity);

    void deleteItem(Long itemId, Integer quantity);

    void makeOrder();
}
