package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
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
    public List<Order> findAll() {
        return orderMapper.bulkEntity2domain(orderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderMapper.entity2domain(orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("order is not found", "id=" + id)));
    }

    @Override
    @Transactional
    public void create(List<Item> items) {
        var total = items.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        var orderEntity = orderRepository.save(OrderEntity.builder().total(total).build());
        var itemEntities = itemMapper.bulkDomain2entity(items).stream().peek(obj -> obj.setOrderId(orderEntity.getId())).toList();
        orderEntity.setItemEntities(itemRepository.saveAll(itemEntities));
        orderMapper.entity2domain(orderEntity);
    }

    @Override
    public Integer getTotal() {
        return findAll().stream().mapToInt(Order::getTotal).sum();
    }
}
