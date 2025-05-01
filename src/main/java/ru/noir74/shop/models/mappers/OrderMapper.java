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
    default Mono<OrderDto> monoDomain2monoDto(Mono<Order> input) {
        return input.map(this::domain2dto);
    }

    default Mono<Order> monoDto2monoDomain(Mono<OrderDto> input) {
        return input.map(this::dto2domain);
    }

    default Mono<OrderEntity> monoDomain2monoEntity(Mono<Order> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Order> monoEntity2monoDomain(Mono<OrderEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<Order> fluxEntity2fluxDomain(Flux<OrderEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<OrderDto> fluxDomain2fluxDto(Flux<Order> input) {
        return input.map(this::domain2dto);
    }
}
