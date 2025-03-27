package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Order> getALl() {
        return orderMapper.bulkEntity2domain(orderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Order get(Long id) {
        return orderMapper.entity2domain(orderRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public Order create(List<Item> items) {
        return orderMapper.entity2domain(orderRepository.save(
                OrderEntity
                        .builder()
                        .itemEntities(itemRepository.saveAll(itemMapper.bulkDomain2entity(items)))
                        .build()));
    }
}
