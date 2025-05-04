package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.entity.ImageEntity;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageEntity domain2entity(Image input);

    Image entity2domain(ImageEntity input);

    default Mono<Image> monoEntity2monoDomain(Mono<ImageEntity> input) {
        return input.map(this::entity2domain);
    }
}
