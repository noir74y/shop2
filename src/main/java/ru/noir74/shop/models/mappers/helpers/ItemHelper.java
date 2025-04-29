package ru.noir74.shop.models.mappers.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.mappers.generic.GenericItemMapper;

@Component
@RequiredArgsConstructor
public class ItemHelper {
    private final GenericItemMapper itemMapper;
}
