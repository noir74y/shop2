package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.generic.GenericOrderMapper;
import ru.noir74.shop.models.mappers.helpers.OrderMapperHelper;

@Mapper(componentModel = "spring", uses = OrderMapperHelper.class)
public interface OrderMapper extends GenericOrderMapper {
    default Mono<OrderDto> monoDomain2monoDto(Mono<Order> input) {
        return input.map(this::domain2dto);
    }

    default Mono<Order> monoDto2monoDomain(Mono<OrderDto> input) {
        return input.map(this::dto2domain);
    }

    default Mono<OrderEntity> monoDomain2monoEntity(Mono<Order> input) {
        return input.map(this::domain2entity);
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
