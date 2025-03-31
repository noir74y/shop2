package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(Long id);

    void create(List<Item> itemList);

    Integer getTotal();
}
