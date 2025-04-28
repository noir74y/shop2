package ru.noir74.shop.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.noir74.shop.models.entity.ImageEntity;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<ImageEntity, Long> {
}
