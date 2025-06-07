package ru.noir74.shop.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.noir74.shop.models.entity.ProductEntity;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    @Query("SELECT * FROM store.products ORDER BY :sort OFFSET :offset LIMIT :limit")
    Flux<ProductEntity> findAllWithSortAndPagination(String sort, long offset, int limit);
}
