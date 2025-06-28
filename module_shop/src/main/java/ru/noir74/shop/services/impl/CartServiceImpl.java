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
    public Flux<Item> findAll(String userName) {
        return cartRepository
                .findAll(userName)
                .as(fluxItemEntity -> itemMapper.fluxEntity2fluxDomain(fluxItemEntity, itemMapperHelper));
    }

    @Override
    public Mono<Integer> getQuantityOfProduct(Long productId, String username) {
        return cartRepository.getQuantityOfProduct(productId, username);
    }

    @Override
    public Mono<Void> addToCart(Long productId, String userName) {
        return productRepository.findById(productId)
                .flatMap(productEntity ->
                        cartRepository.insert(
                                ItemEntity.builder()
                                        .productId(productEntity.getId())
                                        .quantity(1)
                                        .price(productEntity.getPrice())
                                        .build(), userName
                        )
                );
    }

    @Override
    public Mono<Void> addToCart(Long productId, Integer quantity, String userName) {
        return productRepository.findById(productId)
                .flatMap(productEntity ->
                        cartRepository.insert(
                                ItemEntity.builder()
                                        .productId(productEntity.getId())
                                        .quantity(quantity)
                                        .price(productEntity.getPrice())
                                        .build(), userName
                        )
                );
    }

    @Override
    public Mono<Void> removeFromCart(Long productId, String userName) {
        return cartRepository
                .findAll(userName)
                .filter(itemEntity -> itemEntity.getProductId().equals(productId))
                .flatMap(obj -> cartRepository.delete(obj, userName).then())
                .then();
    }

    @Override
    public Mono<Void> setQuantity(Long productId, Integer quantity, String userName) {
        return cartRepository
                .findAll(userName)
                .filter(itemEntity -> itemEntity.getProductId().equals(productId))
                .flatMap(itemEntity -> {
                    itemEntity.setQuantity(quantity);
                    return cartRepository.replace(itemEntity, userName);
                }).then();
    }

    @Override
    public Mono<Integer> getTotal(String userName) {
        return findAll(userName).map(Item::getTotal).reduce(0, Integer::sum);
    }

    @Override
    public Mono<Void> makeOrder(String userName) {
        return cartRepository.findAll(userName)
                .as(fluxItemEntity -> itemMapper.fluxEntity2fluxDomain(fluxItemEntity, itemMapperHelper))
                .transform(obj -> orderService.save(obj, userName))
                .then(cartRepository.deleteAll(userName));
    }

    @Override
    public Mono<Boolean> ifProductInCart(Long productId) {
        return cartRepository.ifProductInAnyCart(productId);
    }
}
