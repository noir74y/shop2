package ru.noir74.shop.models.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "products", schema = "store")
public class ProductEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private Integer price;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
}
