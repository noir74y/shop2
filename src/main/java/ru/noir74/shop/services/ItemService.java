package ru.noir74.shop.services;

import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.domain.Item;

import java.util.List;

public interface ItemService {
    List<Item> getPage(Integer page, Integer size, ItemSorting sort);

    Item get(Long id);

    Long create(Item item);

    void update(Item item);

    void delete(Long id);
}
