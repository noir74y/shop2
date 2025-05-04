package ru.noir74.shop.models.mappers;

import org.mapstruct.Context;
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
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "product", target = "productId", qualifiedByName = "getProductId")
    @Mapping(target = "orderId", expression = "java(orderId)")
    ItemEntity domain2entity(Item input, @Context Long orderId);

    @Mapping(target = "product", ignore = true)
    Item entity2domain(ItemEntity input);

    @Mapping(target = "total", expression = "java(input.getTotal())")
    ItemDto domain2dto(Item input);

    default Flux<ItemEntity> fluxDomain2fluxEntity(Flux<Item> input, Long orderId) {
        return input.flatMap(item -> Mono.just(this.domain2entity(item, orderId)));
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