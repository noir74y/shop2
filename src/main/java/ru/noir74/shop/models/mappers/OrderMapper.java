package ru.noir74.shop.models.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.domain.OrderItem;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.entity.OrderItemEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;
    private final OrderItemMapper orderItemMapper;

    @PostConstruct
    private void setup() {
        Converter<List<OrderItem>, List<OrderItemEntity>> orderItemList2orderItemListEntityConverter =
                list -> orderItemMapper.bulkDomain2entity(list.getSource());
        Converter<List<OrderItemEntity>, List<OrderItem>> orderItemListEntity2orderItemListConverter =
                list -> orderItemMapper.bulkEntity2domain(list.getSource());

        TypeMap<Order, OrderEntity> order2orderEntityMapper = modelMapper.createTypeMap(Order.class, OrderEntity.class);
        TypeMap<OrderEntity, Order> orderEntity2orderMapper = modelMapper.createTypeMap(OrderEntity.class, Order.class);

        order2orderEntityMapper.addMappings(modelMapper ->
                modelMapper.using(orderItemList2orderItemListEntityConverter)
                        .map(Order::getOrderItems, OrderEntity::setOrderItemEntities));

        orderEntity2orderMapper.addMappings(modelMapper ->
                modelMapper.using(orderItemListEntity2orderItemListConverter)
                        .map(OrderEntity::getOrderItemEntities, Order::setOrderItems));
    }

    public Order dto2domain(OrderDto dto) {
        return Optional.ofNullable(dto).map(obj -> modelMapper.map(obj, Order.class)).orElse(null);
    }

    public OrderDto domain2dto(Order domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, OrderDto.class)).orElse(null);
    }

    public OrderEntity domain2entity(Order domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, OrderEntity.class)).orElse(null);
    }

    public Order entity2domain(OrderEntity entity) {
        return Optional.ofNullable(entity).map(obj -> modelMapper.map(obj, Order.class)).orElse(null);
    }

    public List<Order> bulkEntity2domain(List<OrderEntity> entities) {
        return entities.stream()
                .map(this::entity2domain)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<OrderDto> bulkDomain2Dto(List<Order> domains) {
        return domains.stream()
                .map(this::domain2dto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
