package ru.noir74.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.ItemImage;
import ru.noir74.shop.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public ItemImage findImageById(Long itemId) {
        return null;
    }

    @Override
    public void setImageById(ItemImage ItemImage) {

    }
}
