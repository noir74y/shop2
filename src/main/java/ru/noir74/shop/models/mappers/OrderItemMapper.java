package ru.noir74.shop.models.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.domain.OrderItem;
import ru.noir74.shop.models.dto.ItemDtoReq;
import ru.noir74.shop.models.dto.ItemDtoResp;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.OrderItemEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ModelMapper modelMapper;


    public OrderItemEntity domain2entity(OrderItem domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, OrderItemEntity.class)).orElse(null);
    }

    public OrderItem entity2domain(OrderItemEntity entity) {
        return Optional.ofNullable(entity).map(obj -> modelMapper.map(obj, OrderItem.class)).orElse(null);
    }

    public List<OrderItemEntity> bulkDomain2entity(List<OrderItem> domains) {
        return domains.stream()
                .map(this::domain2entity)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<OrderItem> bulkEntity2domain(List<OrderItemEntity> entities) {
        return entities.stream()
                .map(this::entity2domain)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
