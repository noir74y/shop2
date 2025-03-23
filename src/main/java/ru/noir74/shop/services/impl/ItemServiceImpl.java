package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.services.ItemService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<Item> getPage(Integer page, Integer size, ItemSorting sort) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "title"));
        return itemMapper.bulkEntity2domain(itemRepository.findAll(pageable).stream().collect(Collectors.toCollection(LinkedList::new)));
    }

    @Override
    public Item get(Long id) {
        return itemMapper.entity2domain(itemRepository.findById(id).orElse(null));
    }

    @Override
    public Long create(Item item) {
        return itemMapper.entity2domain(itemRepository.save(itemMapper.domain2entity(item))).getId();
    }

    @Override
    public void update(Item item) {
        itemRepository.save(itemMapper.domain2entity(item));
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
