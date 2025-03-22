package ru.noir74.shop.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders", schema = "store")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}