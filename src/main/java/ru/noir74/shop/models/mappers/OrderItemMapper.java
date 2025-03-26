package ru.noir74.shop.models.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.OrderItem;
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
    private final ItemMapper itemMapper;

    @PostConstruct
    private void setup() {
        Converter<Item, ItemEntity> item2itemEntityConverter = obj -> itemMapper.domain2entity(obj.getSource());
        Converter<ItemEntity, Item> itemEntity2itemConverter = obj -> itemMapper.entity2domain(obj.getSource());

        TypeMap<OrderItem, OrderItemEntity> item2itemEntityMapper = modelMapper.createTypeMap(OrderItem.class, OrderItemEntity.class);
        TypeMap<OrderItemEntity, OrderItem> itemEntity2itemMapper = modelMapper.createTypeMap(OrderItemEntity.class, OrderItem.class);

        item2itemEntityMapper.addMappings(modelMapper ->
                modelMapper.using(item2itemEntityConverter).map(OrderItem::getItem, OrderItemEntity::setItemEntity));

        itemEntity2itemMapper.addMappings(modelMapper ->
                modelMapper.using(itemEntity2itemConverter).map(OrderItemEntity::getItemEntity, OrderItem::setItem));
    }

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
