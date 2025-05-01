package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.entity.ImageEntity;
import ru.noir74.shop.models.mappers.generic.GenericImageMapper;

@Mapper(componentModel = "spring")
public interface ImageMapper extends GenericImageMapper {
    default Mono<ImageEntity> monoDomain2monoEntity(Mono<Image> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Image> monoEntity2monoDomain(Mono<ImageEntity> input) {
        return input.map(this::entity2domain);
    }
}
