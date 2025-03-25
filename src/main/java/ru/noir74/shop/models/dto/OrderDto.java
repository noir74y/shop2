package ru.noir74.shop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.noir74.shop.models.domain.OrderItem;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private List<OrderItem> orderItems;

    public Integer getOrderCost() {
        return orderItems.stream().mapToInt(obj -> obj.getItem().getPrice() * obj.getQuantity()).sum();
    }
}