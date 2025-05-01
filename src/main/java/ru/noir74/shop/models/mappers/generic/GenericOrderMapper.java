package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface GenericOrderMapper {
    @Mapping(target = "itemsDto", ignore = true)
    OrderDto domain2dto(Order input);

    @Mapping(target = "items", ignore = true)
    Order dto2domain(OrderDto input);

    OrderEntity domain2entity(Order input);

    @Mapping(target = "items", ignore = true)
    Order entity2domain(OrderEntity input);
}
