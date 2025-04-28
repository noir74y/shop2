package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "images", schema = "store")
public class ImageEntity {
    @Id
    private Long id;

    private byte[] image;

    @Column("image_name")
    private String imageName;

    @Column("product_id")
    private Long productId;
}
