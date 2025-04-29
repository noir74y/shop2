package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.services.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
//    private final OrderRepository orderRepository;
//    private final ItemRepository itemRepository;

//    @Override
//    @Transactional(readOnly = true)
//    public Flux<Order> findAll() {
//        return orderMapper.bulkEntity2domain(orderRepository.findAll());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Mono<Order> findById(Long id) {
//        return orderMapper.entity2domain(orderRepository
//                .findById(id)
//                .orElseThrow(() -> new NotFoundException("order is not found", "id=" + id)));
//    }
//
//    @Override
//    @Transactional
//    public void create(List<Item> items) {
//        var total = items.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
//        var orderEntity = orderRepository.save(OrderEntity.builder().total(total).build());
//        var itemEntities = itemMapper.bulkDomain2entity(items).stream().peek(obj -> obj.setOrderId(orderEntity.getId())).toList();
//        orderEntity.setItemEntities(itemRepository.saveAll(itemEntities));
//        orderMapper.entity2domain(orderEntity);
//    }
//
//    @Override
//    public Integer getTotal() {
//        return findAll().stream().mapToInt(Order::getTotal).sum();
//    }
}
