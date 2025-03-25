package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.domain.OrderItem;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.OrderItemMapper;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.repositories.OrderItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

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
    public Long create(List<OrderItem> orderItemList) {
        //var orderEntity = orderRepository.save(OrderEntity.builder().build());
        //var orderItems = orderItemList.stream().map(obj -> obj.setOrderId(orderId)).collect(Collectors.toSet());
        return orderRepository.save(
                OrderEntity.builder()
                        .orderItemEntities(orderItemMapper.bulkDomain2entity(orderItemList))
                        .build()).getId();
    }
}
