package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Product product;
    private Integer quantity;
    private Integer price;

    public Integer getQuantity() {
        return Optional.ofNullable(quantity).orElse(0);
    }

    public Integer getPrice() {
        return Optional.ofNullable(price).orElse(0);
    }

    public Integer getTotal() {
        return getPrice() * getQuantity();
    }
}
