package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.models.mappers.helpers.ItemMapperHelper;
import ru.noir74.shop.repositories.CartRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ItemMapper itemMapper;
    private final ItemMapperHelper itemMapperHelper;
    private final CartRepository cartRepository;
    public final ProductRepository productRepository;
    private final OrderService orderService;

    @Override
    public Flux<Item> findAll() {
        return cartRepository
                .findAll()
                .as(fluxItemEntity -> itemMapper.fluxEntity2fluxDomain(fluxItemEntity, itemMapperHelper));
    }

    @Override
    public Mono<Integer> getQuantityOfProduct(Long productId) {
        return cartRepository.getQuantityOfProduct(productId);
    }

    @Override
    public Mono<Void> addToCart(Long productId) {
        return productRepository.findById(productId)
                .flatMap(productEntity ->
                        cartRepository.insert(
                                ItemEntity.builder()
                                        .productId(productEntity.getId())
                                        .quantity(1)
                                        .price(productEntity.getPrice())
                                        .build()
                        )
                );
    }

    @Override
    public Mono<Void> addToCart(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .flatMap(productEntity ->
                        cartRepository.insert(
                                ItemEntity.builder()
                                        .productId(productEntity.getId())
                                        .quantity(quantity)
                                        .price(productEntity.getPrice())
                                        .build()
                        )
                );
    }

    @Override
    public Mono<Void> removeFromCart(Long productId) {
        return cartRepository
                .findAll()
                .filter(itemEntity -> itemEntity.getProductId().equals(productId))
                .flatMap(obj -> cartRepository.delete(obj).then())
                .then();
    }

    @Override
    public Mono<Void> setQuantity(Long productId, Integer quantity) {
        return cartRepository
                .findAll()
                .filter(itemEntity -> itemEntity.getProductId().equals(productId))
                .flatMap(itemEntity -> {
                    itemEntity.setQuantity(quantity);
                    return cartRepository.replace(itemEntity);
                }).then();
    }

    @Override
    public Mono<Integer> getTotal() {
        return findAll().map(Item::getTotal).reduce(0, Integer::sum);
    }

    @Override
    public Mono<Void> makeOrder() {
        return cartRepository.findAll()
                .as(fluxItemEntity -> itemMapper.fluxEntity2fluxDomain(fluxItemEntity, itemMapperHelper))
                .transform(orderService::save)
                .then(cartRepository.deleteAll());
    }

    @Override
    public Mono<Boolean> ifProductInCart(Long productId) {
        return cartRepository.ifProductInCart(productId);
    }
}
