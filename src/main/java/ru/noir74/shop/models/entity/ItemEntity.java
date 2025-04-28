package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.noir74.shop.models.generic.GenericEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "items", schema = "store")
public class ItemEntity extends GenericEntity {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("product_id")
    private Long productId;

    private Integer quantity;
    private Integer price;
}
