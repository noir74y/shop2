package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.noir74.shop.models.domain.Item;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private List<Item> items;

    public Integer getOrderCost() {
        return items.stream().mapToInt(obj -> obj.getProduct().getPrice() * obj.getQuantity()).sum();
    }
}