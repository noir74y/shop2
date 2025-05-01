package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.helpers.ItemMapperHelper;

@Mapper(componentModel = "spring", uses = ItemMapperHelper.class)
public interface ItemMapper {
    @Mapping(source = "product", target = "productId", qualifiedByName = "getProductId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    ItemEntity domain2entity(Item input);

    @Mapping(target = "product", ignore = true)
    Item entity2domain(ItemEntity input);

    ItemDto domain2dto(Item input);

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