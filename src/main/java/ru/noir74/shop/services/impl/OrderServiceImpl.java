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
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
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
    public Long create(List<Item> itemList) {
        //var orderEntity = orderRepository.save(OrderEntity.builder().build());
        //var orderItems = orderItemList.stream().map(obj -> obj.setOrderId(orderId)).collect(Collectors.toSet());
        return orderRepository.save(
                OrderEntity.builder()
                        .itemEntities(itemMapper.bulkDomain2entity(itemList))
                        .build()).getId();
    }
}
