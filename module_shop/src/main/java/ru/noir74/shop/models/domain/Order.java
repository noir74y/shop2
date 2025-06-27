package ru.noir74.shop.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String user;
    private List<Item> items;

    public Integer getTotal() {
        return items.stream().mapToInt(Item::getTotal).sum();
    }
}
