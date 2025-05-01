package ru.noir74.shop.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product dtoReq2domain(ProductDtoReq input);

    @Mapping(target = "quantity", ignore = true)
    ProductDtoResp domain2dtoResp(Product input);

    ProductEntity domain2entity(Product input);

    @Mapping(target = "file", ignore = true)
    Product entity2domain(ProductEntity input);

    default Mono<Product> monoDtoReq2monoDomain(Mono<ProductDtoReq> input) {
        return input.map(this::dtoReq2domain);
    }

    default Mono<ProductDtoResp> monoDomain2monoDtoResp(Mono<Product> input) {
        return input.map(this::domain2dtoResp);
    }

    default Mono<ProductEntity> monoDomain2monoEntity(Mono<Product> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Product> monoEntity2monoDomain(Mono<ProductEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<Product> fluxEntity2fluxDomain(Flux<ProductEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<ProductDtoResp> fluxDomain2fluxDtoResp(Flux<Product> input) {
        return input.map(this::domain2dtoResp);
    }
}