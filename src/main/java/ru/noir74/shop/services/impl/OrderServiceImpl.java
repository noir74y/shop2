package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<Order> getALl() {
        return orderMapper.bulkEntity2domain(orderRepository.findAll());
    }

    @Override
    public Order get(Long id) {
        return orderMapper.entity2domain(orderRepository.findById(id).orElse(null));
    }
}
