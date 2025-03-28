package ru.noir74.shop.models.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "images", schema = "store")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image", columnDefinition = "BYTEA")
    private byte[] image;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;
}
