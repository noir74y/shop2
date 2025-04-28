package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.noir74.shop.models.generic.GenericEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "products", schema = "store")
public class ProductEntity extends GenericEntity {
    @Id
    private Long id;

    private String title;
    private Integer price;
    private String description;
}
