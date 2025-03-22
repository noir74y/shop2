package ru.noir74.shop.models.dto;

import ru.noir74.shop.models.domain.Item;

import java.util.List;

public class OrderDtoResp {
    private Long id;
    private List<Item> items;
}
