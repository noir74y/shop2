package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;

@Mapper(componentModel = "spring")
public interface GenericItemMapper {
    ItemEntity domain2entity(Item input);

    Item entity2domain(ItemEntity input);

    ItemDto domain2dto(Item input);
}
