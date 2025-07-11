package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.helpers.OrderMapperHelper;

@Mapper(componentModel = "spring", uses = OrderMapperHelper.class)
public interface OrderMapper {
    @Mapping(source = "items", target = "itemsDto", qualifiedByName = "getItemsDto")
    @Mapping(target = "total", expression = "java(input.getTotal())")
    OrderDto domain2dto(Order input);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "username", ignore = true)
    Order dto2domain(OrderDto input);

    OrderEntity domain2entity(Order input);

    @Mapping(target = "items", ignore = true)
    Order entity2domain(OrderEntity input);

    default Mono<OrderDto> monoDomain2monoDto(Mono<Order> input) {
        return input.map(this::domain2dto);
    }

    default Mono<Order> monoEntity2monoDomain(Mono<OrderEntity> input, OrderMapperHelper orderMapperHelper) {
        return input.flatMap(orderEntity ->
                orderMapperHelper.getItems(orderEntity.getId())
                        .map(items -> {
                            Order order = entity2domain(orderEntity);
                            order.setItems(items);
                            return order;
                        })
        );
    }

    default Flux<Order> fluxEntity2fluxDomain(Flux<OrderEntity> input, OrderMapperHelper orderMapperHelper) {
        return input.flatMap(orderEntity ->
                orderMapperHelper.getItems(orderEntity.getId())
                        .map(items -> {
                            Order order = entity2domain(orderEntity);
                            order.setItems(items);
                            return order;
                        })
        );
    }

    default Flux<OrderDto> fluxDomain2fluxDto(Flux<Order> input) {
        return input.map(this::domain2dto);
    }
}
