package ru.noir74.shop.models.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.ItemImage;
import ru.noir74.shop.models.entity.ImageEntity;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImageMapper {
    private final ModelMapper modelMapper;

    public ImageEntity domain2entity(ItemImage domain) {
        return Optional.ofNullable(domain)
                .map(obj -> modelMapper.map(obj, ImageEntity.class))
                .orElse(null);
    }

    public ItemImage entity2domain(ImageEntity entity) {
        return Optional.ofNullable(entity)
                .map(obj -> modelMapper.map(obj, ItemImage.class))
                .orElse(null);
    }

}
