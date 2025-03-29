package ru.noir74.shop.models.mappers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;

    @PostConstruct
    private void setup() {
        Converter<Product, ProductEntity> domain2entityConverter = obj -> productMapper.domain2entity(obj.getSource());
        Converter<ProductEntity, Product> entity2domainConverter = obj -> productMapper.entity2domain(obj.getSource());

        TypeMap<Item, ItemEntity> domain2entityMapper = modelMapper.createTypeMap(Item.class, ItemEntity.class);
        TypeMap<ItemEntity, Item> entity2domainMapper = modelMapper.createTypeMap(ItemEntity.class, Item.class);

        domain2entityMapper.addMappings(modelMapper ->
                modelMapper.using(domain2entityConverter).map(Item::getProduct, ItemEntity::setProductEntity));

        entity2domainMapper.addMappings(modelMapper ->
                modelMapper.using(entity2domainConverter).map(ItemEntity::getProductEntity, Item::setProduct));
    }

    public ItemEntity domain2entity(Item domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ItemEntity.class)).orElse(null);
    }

    public Item entity2domain(ItemEntity entity) {
        return Optional.ofNullable(entity).map(obj -> modelMapper.map(obj, Item.class)).orElse(null);
    }

    public ItemDto domain2dto(Item domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ItemDto.class)).orElse(null);
    }

    public List<ItemEntity> bulkDomain2entity(List<Item> domains) {
        return domains.stream().map(this::domain2entity).toList();
    }

    public List<Item> bulkEntity2domain(List<ItemEntity> entities) {
        return entities.stream().map(this::entity2domain).toList();
    }

    public List<ItemDto> bulkDomain2dto(List<Item> domains) {
        return domains.stream().map(this::domain2dto).toList();
    }

}
