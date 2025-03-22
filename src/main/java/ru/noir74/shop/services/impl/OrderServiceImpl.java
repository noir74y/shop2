package ru.noir74.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getALl() {
        return List.of();
    }

    @Override
    public Order get(Long id) {
        return null;
    }
}
