package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.openapitools.client.model.Balance;
import org.openapitools.client.model.PaymentRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.client.api.PaymentApi;
import ru.noir74.shop.configurations.SecurityConfig;
import ru.noir74.shop.models.dto.ItemDto;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.services.CartService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CartHandler {
    private final CartService cartService;
    private final ItemMapper itemMapper;
    private final PaymentApi paymentApi;
    private final SecurityConfig securityConfig;

    public Mono<ServerResponse> viewCart(ServerRequest request) {

        Mono<Integer> totalMono = cartService.getTotal();
        Mono<Balance> balanceMono = paymentApi.getBalance();
        Mono<List<ItemDto>> itemsDtoMono = cartService.findAll()
                .as(itemMapper::fluxDomain2fluxDto)
                .collectList();

        Mono<Map<String, Object>> loginLoginAttributesMono = securityConfig.prepareLoginLogout();

        return Mono.zip(itemsDtoMono, totalMono, balanceMono, loginLoginAttributesMono).flatMap(tuple -> {
            List<ItemDto> itemsDto = tuple.getT1();
            Integer total = tuple.getT2();
            Balance balance = tuple.getT3();
            Map<String, Object> loginLoginAttributes = tuple.getT4();

            Map<String, Object> modelData = new HashMap<>();
            modelData.put("items", itemsDto);
            modelData.put("total", total);
            modelData.put("balance", balance.getAmount());
            modelData.putAll(loginLoginAttributes);

            return ServerResponse.ok().render("cart", modelData);
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
        return cartService.getTotal()
                .flatMap
                        (total -> {
                                    if (total == 0) return ServerResponse.noContent().build();
                                    else return paymentApi.makePayment(new PaymentRequest().amount(total));
                                }
                        )
                .then(cartService.makeOrder())
                .then(ServerResponse.seeOther(URI.create("/order")).build());
    }
}
