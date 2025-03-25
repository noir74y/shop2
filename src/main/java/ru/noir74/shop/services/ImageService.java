package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Image;

public interface ImageService {
    Image findImageById(Long itemId);

    void setImageById(Image Image);
}
