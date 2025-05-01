package ru.noir74.shop.models.mappers.helpers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.repositories.ProductRepository;

@Component
@RequiredArgsConstructor
public class ItemMapperHelper {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Named("getProduct")
    public Mono<Product> getProduct(Long productId) {
        return productRepository.findById(productId).map(productMapper::entity2domain);
    }

    @Named("getProductId")
    public Long getProductId(Product product) {
        return product.getId();
    }
}
