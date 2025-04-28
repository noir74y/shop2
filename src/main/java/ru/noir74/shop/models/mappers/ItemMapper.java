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
    default Mono<ItemEntity> domain2entity(Mono<Item> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Item> entity2domain(Mono<ItemEntity> input) {
        return input.map(this::entity2domain);
    }

    default Mono<ItemDto> dto2domain(Mono<Item> input) {
        return input.map(this::domain2dto);
    }

    default Flux<ItemEntity> bulkDomain2entity(Flux<Item> input) {
        return input.map(this::domain2entity);
    }

    default Flux<Item> bulkEntity2domain(Flux<ItemEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<ItemDto> bulkDomain2dto(Flux<Item> input) {
        return input.map(this::domain2dto);
    }

}
