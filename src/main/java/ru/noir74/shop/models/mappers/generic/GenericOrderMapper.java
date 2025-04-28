package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface GenericOrderMapper {
    OrderDto domain2dto(Order input);

    Order dto2domain(OrderDto input);

    OrderEntity domain2entity(Order input);

    Order entity2domain(OrderEntity input);
}
