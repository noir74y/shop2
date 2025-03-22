package ru.noir74.shop.services;

import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.ItemImage;

@Service
public interface ImageService {
    ItemImage findImageById(Long itemId);

    void setImageById(ItemImage ItemImage);
}
