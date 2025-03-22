package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Item;

import java.util.List;

public interface ItemService {
    List<Item> getPage(String page, String size, String sort);

    Item get(Long id);

    Long create(Item item);

    void update(Item item);

    void delete(Long id);
}
