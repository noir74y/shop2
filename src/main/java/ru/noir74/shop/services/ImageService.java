package ru.noir74.shop.services;

import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Image;

public interface ImageService {
    Mono<Image> findImageById(Long id);

    Mono<Void> setImage(Image Image);

    Mono<Void> deleteById(Long id);
}
