package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "items", schema = "store")
public class ItemEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "order_id")
//    private Long orderId;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private ProductEntity productEntity;
//
//    @Positive
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Positive
//    @Column(nullable = false)
//    private Integer price;

}
