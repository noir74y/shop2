package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.OrderDto;

import java.util.List;

public interface CartService {
    List<OrderDto> get();

    void addItem(Long itemId);

    void removeFromCart(Long itemId);

    void setQuantity(Long itemId, Integer quantity);

    void makeOrder();
}
