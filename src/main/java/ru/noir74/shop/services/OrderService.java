package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    Order get(Long id);

    Order create(List<Item> itemList);
}
