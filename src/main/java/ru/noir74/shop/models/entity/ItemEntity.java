package ru.noir74.shop.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "items", schema = "store")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "itemEntity", cascade = CascadeType.PERSIST)
    private Set<OrdersItemsEntity> ordersItemsEntities;
}
