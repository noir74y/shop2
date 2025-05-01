package ru.noir74.shop.models.mappers.helpers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.repositories.ItemRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapperHelper {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ItemMapperHelper itemMapperHelper;

    @Named("getItems")
    public Mono<List<Item>> getItems(Long orderId) {
        return itemRepository
                .findByOrderId(orderId)
                .as(fluxItemEntity -> itemMapper.fluxEntity2fluxDomain(fluxItemEntity, itemMapperHelper))
                .collectList();
    }

    @Named("getItemsDto")
    public List<ItemDto> getItemsDto(List<Item> items) {
        return items.stream().map(itemMapper::domain2dto).toList();
    }
}
