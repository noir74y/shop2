package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.noir74.shop.models.domain.OrderItem;
import ru.noir74.shop.models.entity.OrderItemEntity;
import ru.noir74.shop.models.mappers.OrderItemMapper;
import ru.noir74.shop.repositories.CartRepository;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    public final ItemRepository itemRepository;
    private final OrderService orderService;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> get() {
        return orderItemMapper.bulkEntity2domain(cartRepository.findAll());
    }

    @Override
    public void addToCart(Long itemId) {
        cartRepository.insert(OrderItemEntity.builder()
                .itemEntity(itemRepository.findById(itemId).orElse(null))
                .quantity(1)
                .build());
    }

    @Override
    public void removeFromCart(Long itemId) {
        cartRepository.delete(cartRepository
                .findAll()
                .stream()
                .filter(obj -> obj.getItemEntity().getId().equals(itemId))
                .findFirst().orElse(null));
    }

    @Override
    public void setQuantity(Long itemId, Integer quantity) {
        var orderItemEntity = cartRepository
                .findAll()
                .stream()
                .filter(obj -> obj.getItemEntity().getId().equals(itemId))
                .findFirst().orElse(null);
        Optional.ofNullable(orderItemEntity).ifPresent(obj -> {
            obj.setQuantity(quantity);
            cartRepository.replace(obj);
        });
    }

    @Override
    public void makeOrder() {
        orderService.create(orderItemMapper.bulkEntity2domain(cartRepository.findAll()));
    }
}
