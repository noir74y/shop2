package ru.noir74.shop.models.entity;

import jakarta.persistence.*;

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

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] image;
}
