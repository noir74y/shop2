package ru.noir74.shop.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "images", schema = "store")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @OneToOne(mappedBy = "imageEntity")
    private ItemEntity itemEntity;
}
