package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.generic.GenericItemMapper;

@Mapper(componentModel = "spring")
public interface ItemMapper extends GenericItemMapper {
    default Mono<ItemEntity> monoDomain2monoEntity(Mono<Item> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Item> monoEntity2monoDomain(Mono<ItemEntity> input) {
        return input.map(this::entity2domain);
    }

    default Mono<ItemDto> monoDto2monoDomain(Mono<Item> input) {
        return input.map(this::domain2dto);
    }

    default Flux<ItemEntity> fluxDomain2fluxEntity(Flux<Item> input) {
        return input.map(this::domain2entity);
    }

    default Flux<Item> fluxEntity2fluxDomain(Flux<ItemEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<ItemDto> fluxDomain2fluxDto(Flux<Item> input) {
        return input.map(this::domain2dto);
    }

}
