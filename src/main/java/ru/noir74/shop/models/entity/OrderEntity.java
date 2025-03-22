package ru.noir74.shop.models.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "orders", schema = "store")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OrdersItemsEntity> ordersItemsEntities;
}