package ru.noir74.shop.models.dto;

import org.springframework.core.SpringVersion;
import ru.noir74.shop.models.domain.Item;

import java.util.List;

public class OrderDto {
    private Long id;
    private List<ItemDtoResp> items;
    private Integer totalCost;
}
