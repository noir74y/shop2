package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public Flux<Order> findAll() {
        return orderRepository.findAll().as(orderMapper::fluxEntity2fluxDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("order is not found", "id=" + id)))
                .as(orderMapper::monoEntity2monoDomain);
    }

    @Override
    @Transactional
    public Mono<Void> create(Flux<Item> items) {
        Flux<Item> cachedItems = items.cache();

        return cachedItems
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0, Integer::sum)
                .flatMap(total ->
                        orderRepository.save(OrderEntity.builder().total(total).build())
                                .flatMap(order ->
                                        cachedItems
                                                .map(item -> {
                                                    ItemEntity entity = itemMapper.domain2entity(item);
                                                    entity.setOrderId(order.getId());
                                                    return entity;
                                                })
                                                .transform(itemRepository::saveAll)
                                                .then()
                                )
                );
    }

    @Override
    public Mono<Integer> getTotal() {
        return findAll().map(Order::getTotal).reduce(0, Integer::sum).defaultIfEmpty(0);
    }
}
