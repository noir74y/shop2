package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.repositories.CartRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    public final ProductRepository productRepository;
    private final OrderService orderService;
    private final ItemMapper itemMapper;

    @Override
    public List<Item> get() {
        return itemMapper.bulkEntity2domain(cartRepository.findAll());
    }

    @Override
    public void addToCart(Long itemId) {
        cartRepository.insert(ItemEntity.builder()
                .productEntity(productRepository.findById(itemId).orElse(null))
                .quantity(1)
                .build());
    }

    @Override
    public void removeFromCart(Long itemId) {
        cartRepository
                .findAll()
                .stream()
                .filter(obj -> obj.getProductEntity().getId().equals(itemId))
                .findFirst()
                .ifPresent(cartRepository::delete);
    }

    @Override
    public void setQuantity(Long itemId, Integer quantity) {
        cartRepository
                .findAll()
                .stream()
                .filter(obj -> obj.getProductEntity().getId().equals(itemId))
                .findFirst().ifPresent(obj -> {
                    obj.setQuantity(quantity);
                    cartRepository.replace(obj);
                });
    }

    @Override
    public void makeOrder() {
        orderService.create(itemMapper.bulkEntity2domain(cartRepository.findAll()));
        cartRepository.deleteAll();
    }
}
