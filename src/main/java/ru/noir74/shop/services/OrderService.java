package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Order;

import java.util.List;

public interface OrderService {
    List<Order> getALl();

    Order get(Long id);
}
