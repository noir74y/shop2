package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Product product;
    private Integer quantity;
    private Integer price;
}
