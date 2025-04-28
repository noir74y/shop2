package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoResp {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private Integer quantity;
}
