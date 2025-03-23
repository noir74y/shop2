package ru.noir74.shop.services;

import org.springframework.stereotype.Service;
import ru.noir74.shop.models.dto.OrderDto;

import java.util.List;

@Service
public interface CartService {
    List<OrderDto> get();

    void addToCart(Long itemId);

    void removeFromCart(Long itemId);

    void setQuantity(Long itemId, Integer quantity);

    void makeOrder();
}
