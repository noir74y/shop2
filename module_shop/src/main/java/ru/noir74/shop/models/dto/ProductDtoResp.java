package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

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

    public Integer getQuantity() {
        return Optional.ofNullable(quantity).orElse(0);
    }
}
