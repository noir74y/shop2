package ru.noir74.shop.models.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.entity.ProductEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;

    public Product dtoReq2domain(ProductDtoReq dtoReq) {
        return Optional.ofNullable(dtoReq).map(obj -> modelMapper.map(obj, Product.class)).orElse(null);
    }

    public ProductDtoResp domain2dtoResp(Product domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ProductDtoResp.class)).orElse(null);
    }

    public ProductEntity domain2entity(Product domain) {
        return Optional.ofNullable(domain).map(obj -> modelMapper.map(obj, ProductEntity.class)).orElse(null);
    }

    public Product entity2domain(ProductEntity entity) {
        return Optional.ofNullable(entity).map(obj -> modelMapper.map(obj, Product.class)).orElse(null);
    }

    public List<Product> bulkEntity2domain(List<ProductEntity> entities) {
        return entities.stream()
                .map(this::entity2domain)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<ProductDtoResp> bulkDomain2DtoResp(List<Product> domains) {
        return domains.stream()
                .map(this::domain2dtoResp)
                .collect(Collectors.toCollection(LinkedList::new));
    }

}
