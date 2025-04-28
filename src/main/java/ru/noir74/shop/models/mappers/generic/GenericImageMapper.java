package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.entity.ImageEntity;

@Mapper(componentModel = "spring")
public interface GenericImageMapper {
    ImageEntity domain2entity(Image input);

    Image entity2domain(ImageEntity input);
}
