package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.client.api.PaymentApi;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.models.mappers.helpers.OrderMapperHelper;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.services.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderMapperHelper orderMapperHelper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final PaymentApi paymentApi;

    @Override
    @Transactional(readOnly = true)
    public Flux<Order> findAll() {
        return orderRepository.findAll()
                .as(fluxOrderEntity -> orderMapper.fluxEntity2fluxDomain(fluxOrderEntity, orderMapperHelper));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("order is not found", "id=" + id)))
                .as(monoOrderEntity -> orderMapper.monoEntity2monoDomain(monoOrderEntity, orderMapperHelper));
    }

    @Override
    @Transactional
    public Mono<Void> save(Flux<Item> items) {
        return orderRepository.save(OrderEntity.builder().build())
                .flatMap(orderEntity ->
                        items
                                .transform(flux -> itemMapper.fluxDomain2fluxEntity(flux, orderEntity.getId()))
                                .transform(itemRepository::saveAll)
                                .then()
                );
    }

    @Override
    public Mono<Integer> getTotal() {
        return findAll().map(Order::getTotal).reduce(0, Integer::sum).defaultIfEmpty(0);
    }
}
