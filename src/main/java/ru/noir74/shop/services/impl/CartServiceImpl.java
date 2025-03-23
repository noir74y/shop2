package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OrderService orderService;

    @Override
    public List<OrderDto> get() {
        return List.of();
    }

    @Override
    public void addToCart(Long itemId) {
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
