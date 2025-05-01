package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.generic.GenericItemMapper;
import ru.noir74.shop.models.mappers.helpers.ItemMapperHelper;

@Mapper(componentModel = "spring", uses = ItemMapperHelper.class)
public interface ItemMapper extends GenericItemMapper {
    default Mono<ItemEntity> monoDomain2monoEntity(Mono<Item> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Item> monoEntity2monoDomain(Mono<ItemEntity> input, ItemMapperHelper itemMapperHelper) {
        return input.flatMap(itemEntity ->
                itemMapperHelper.getProduct(itemEntity.getProductId())
                        .map(product -> {
                            Item item = entity2domain(itemEntity);
                            item.setProduct(product);
                            return item;
                        })
        );
    }

    default Mono<ItemDto> monoDto2monoDomain(Mono<Item> input) {
        return input.map(this::domain2dto);
    }

    default Flux<ItemEntity> fluxDomain2fluxEntity(Flux<Item> input) {
        return input.map(this::domain2entity);
    }

    default Flux<Item> fluxEntity2fluxDomain(Flux<ItemEntity> input, ItemMapperHelper itemMapperHelper) {
        return input.flatMap(itemEntity ->
                itemMapperHelper.getProduct(itemEntity.getProductId())
                        .map(product -> {
                            Item item = entity2domain(itemEntity);
                            item.setProduct(product);
                            return item;
                        })
        );
    }

    default Flux<ItemDto> fluxDomain2fluxDto(Flux<Item> input) {
        return input.map(this::domain2dto);
    }
}