package ru.noir74.shop.models.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

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
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<OrderDto> bulkDomain2Dto(List<Order> domains) {
        return domains.stream()
                .map(this::domain2dto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
