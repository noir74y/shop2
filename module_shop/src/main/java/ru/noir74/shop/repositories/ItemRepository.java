package ru.noir74.shop.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.entity.ItemEntity;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<ItemEntity, Long> {
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM store.items WHERE product_id = :productId")
    Mono<Boolean> isProductUsedInItems(@Param("productId") Long productId);

    @Query("SELECT * FROM store.items WHERE order_id = :orderId")
    Flux<ItemEntity> findByOrderId(@Param("orderId") Long orderId);
}
