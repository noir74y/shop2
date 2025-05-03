package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.services.CartService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CartHandler {
    private final CartService cartService;
    private final ItemMapper itemMapper;

    public Mono<ServerResponse> viewCart(ServerRequest request) {
        return cartService.findAll()
                .as(itemMapper::fluxDomain2fluxDto)
                .collectList()
                .zipWith(cartService.getTotal())
                .flatMap(tuple -> {
                    List<ItemDto> items = tuple.getT1();
                    Integer total = tuple.getT2();

                    return ServerResponse.ok()
                            .render("cart", Map.of("items", items, "total", total));
                });
    }

    public Mono<ServerResponse> addToCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return cartService.addToCart(productId)
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> removeFromCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return cartService.removeFromCart(productId)
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> setQuantity(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        Integer quantity = Integer.parseInt(request.pathVariable("quantity"));
        return cartService.setQuantity(productId, quantity)
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> makeOrder(ServerRequest request) {
        return cartService.makeOrder()
                .then(ServerResponse.seeOther(URI.create("/order")).build());
    }
}
