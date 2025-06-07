package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
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
    public Mono<Image> findImageById(Long id) {
        return imageRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("image is not found", "id=" + id)))
                .as(imageMapper::monoEntity2monoDomain);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mono<Void> setImage(Image image) {
        return imageRepository.findById(image.getProductId())
                .flatMap(imageEntity -> {
                    imageEntity.setImage(image.getImage());
                    imageEntity.setImageName(image.getImageName());
                    return imageRepository.save(imageEntity);
                }).switchIfEmpty(Mono.defer(() -> imageRepository.save(imageMapper.domain2entity(image)))
                ).then();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mono<Void> deleteById(Long id) {
        return imageRepository.deleteById(id);
    }
}
