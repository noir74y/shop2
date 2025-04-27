package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orders", schema = "store")
public class OrderEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    Integer total;
//
//    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private List<ItemEntity> itemEntities;
}