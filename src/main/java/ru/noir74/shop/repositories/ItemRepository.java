package ru.noir74.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.noir74.shop.models.entity.ItemEntity;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<ItemEntity, Long> {
//    @Query(value = "SELECT 1 FROM store.items WHERE product_id = :productId LIMIT 1", nativeQuery = true)
//    Long isProductUsesInItems(Long productId);
}
