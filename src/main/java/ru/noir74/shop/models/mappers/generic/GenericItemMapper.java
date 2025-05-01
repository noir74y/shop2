package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.helpers.ItemMapperHelper;

@Mapper(componentModel = "spring", uses = ItemMapperHelper.class)
public interface GenericItemMapper {
    @Mapping(
            source = "product",
            target = "productId",
            qualifiedByName = "getProductId"
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    ItemEntity domain2entity(Item input);

    @Mapping(target = "product", ignore = true)
    Item entity2domain(ItemEntity input);

    ItemDto domain2dto(Item input);
}
