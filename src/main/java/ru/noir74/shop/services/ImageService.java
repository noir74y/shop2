package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.ItemImage;

public interface ImageService {
    ItemImage findImageById(Long itemId);

    void setImageById(ItemImage ItemImage);
}
