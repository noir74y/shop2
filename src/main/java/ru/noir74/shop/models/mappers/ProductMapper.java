package ru.noir74.shop.models.mappers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.entity.ProductEntity;
import ru.noir74.shop.models.mappers.generic.GenericProductMapper;

public interface ProductMapper extends GenericProductMapper {
    default Mono<Product> dtoReq2domain(Mono<ProductDtoReq> input) {
        return input.map(this::dtoReq2domain);
    }

    default Mono<ProductDtoResp> domain2dtoResp(Mono<Product> input) {
        return input.map(this::domain2dtoResp);
    }

    default Mono<ProductEntity> domain2entity(Mono<Product> input) {
        return input.map(this::domain2entity);
    }

    default Mono<Product> entity2domain(Mono<ProductEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<Product> bulkEntity2domain(Flux<ProductEntity> input) {
        return input.map(this::entity2domain);
    }

    default Flux<ProductDtoResp> bulkDomain2DtoResp(Flux<Product> input) {
        return input.map(this::domain2dtoResp);
    }
}