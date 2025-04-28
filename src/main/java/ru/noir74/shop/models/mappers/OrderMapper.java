package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.generic.GenericOrderMapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericOrderMapper {
    default Mono<OrderDto> domain2dto(Mono<Order> input) {
        return input.map(this::domain2dto);
    }

    default Mono<Order> dto2domain(Mono<OrderDto> input) {
        return input.map(this::dto2domain);
    }

    default Mono<OrderEntity> domain2entity(Mono<Order> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Order> entity2domain(Mono<OrderEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<Order> entity2domain(Flux<OrderEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<OrderDto> domain2dto(Flux<Order> input) {
        return input.map(this::domain2dto);
    }
}
