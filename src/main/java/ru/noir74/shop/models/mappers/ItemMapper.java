package ru.noir74.shop.models.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.dto.ItemDtoReq;
import ru.noir74.shop.models.dto.ItemDtoResp;
import ru.noir74.shop.models.entity.ItemEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ModelMapper modelMapper;

    public Item dtoReq2domain(ItemDtoReq dtoReq) {
        return Optional.ofNullable(dtoReq).map(obj -> modelMapper.map(obj, Item.class)).orElse(null);
    }

    public ItemDtoResp domain2dtoResp(Item domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ItemDtoResp.class)).orElse(null);
    }

    public ItemEntity domain2entity(Item domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ItemEntity.class)).orElse(null);
    }

    public Item entity2domain(ItemEntity entity) {
        return Optional.ofNullable(entity).map(obj -> modelMapper.map(obj, Item.class)).orElse(null);
    }

    public List<Item> bulkEntity2domain(List<ItemEntity> entities) {
        return entities.stream()
                .map(this::entity2domain)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<ItemDtoResp> bulkDomain2DtoResp(List<Item> domains) {
        return domains.stream()
                .map(this::domain2dtoResp)
                .collect(Collectors.toCollection(LinkedList::new));
    }

}
