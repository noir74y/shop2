package ru.noir74.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.services.CartService;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public List<OrderDto> get() {
        return List.of();
    }

    @Override
    public void addItem(Long itemId) {
    }

    @Override
    public void removeFromCart(Long itemId) {
    }

    @Override
    public void setQuantity(Long itemId, Integer quantity) {
    }

    @Override
    public void makeOrder() {
    }
}
