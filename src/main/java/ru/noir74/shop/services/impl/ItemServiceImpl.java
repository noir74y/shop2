package ru.noir74.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.services.ItemService;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Override
    public List<Item> getPage(Integer page, Integer size, ItemSorting sort) {
        return null;
    }

    @Override
    public Item get(Long id) {
        return null;
    }

    @Override
    public Long create(Item item) {
        return 0L;
    }

    @Override
    public void update(Item item) {

    }

    @Override
    public void delete(Long id) {

    }
}
