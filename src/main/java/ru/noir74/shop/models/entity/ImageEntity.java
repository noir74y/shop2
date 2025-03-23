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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @Column(nullable = false)
    private String imageName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;
}
