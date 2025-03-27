package ru.noir74.shop.models.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.OrderEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;
    private final ItemMapper itemMapper;

    @PostConstruct
    private void setup() {
        Converter<List<Item>, List<ItemEntity>> domains2entitiesConverter =
                list -> itemMapper.bulkDomain2entity(list.getSource());
        Converter<List<ItemEntity>, List<Item>> entities2domainsConverter =
                list -> itemMapper.bulkEntity2domain(list.getSource());

        TypeMap<Order, OrderEntity> domain2entityMapper = modelMapper.createTypeMap(Order.class, OrderEntity.class);
        TypeMap<OrderEntity, Order> entity2domainMapper = modelMapper.createTypeMap(OrderEntity.class, Order.class);

        domain2entityMapper.addMappings(modelMapper ->
                modelMapper.using(domains2entitiesConverter)
                        .map(Order::getItems, OrderEntity::setItemEntities));

        entity2domainMapper.addMappings(modelMapper ->
                modelMapper.using(entities2domainsConverter)
                        .map(OrderEntity::getItemEntities, Order::setItems));
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
