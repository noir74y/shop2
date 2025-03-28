package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.mappers.ImageMapper;
import ru.noir74.shop.repositories.ImageRepository;
import ru.noir74.shop.services.ImageService;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    @Transactional(readOnly = true)
    public Image findImageById(Long productId) {
        return imageMapper.entity2domain(imageRepository.findById(productId).orElse(null));
    }

    @Override
    @Transactional
    public void setImageById(Image image) {
        if (image.isImageReadyToBeSaved())
            imageRepository.save(imageMapper.domain2entity(image));
    }
}
