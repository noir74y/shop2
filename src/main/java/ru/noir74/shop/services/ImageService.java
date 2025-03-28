package ru.noir74.shop.services;

import ru.noir74.shop.models.domain.Image;

public interface ImageService {
    Image findImageById(Long id);

    void setImageById(Image Image);
}
