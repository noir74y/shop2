package ru.noir74.shop.models.mappers.generic;

import org.mapstruct.Mapper;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface GenericProductMapper {
    Product dtoReq2domain(ProductDtoReq input);

    ProductDtoResp domain2dtoResp(Product input);

    ProductEntity domain2entity(Product input);

    Product entity2domain(ProductEntity input);
}
