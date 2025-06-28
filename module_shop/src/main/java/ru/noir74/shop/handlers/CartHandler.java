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

        return securityConfig.prepareLoginLogout()
                .flatMap(loginLoginAttributes -> {
                    var userName = (String) loginLoginAttributes.getOrDefault("userName", "");

                    Mono<List<ItemDto>> itemsDtoMono = cartService.findAll(userName)
                            .as(itemMapper::fluxDomain2fluxDto)
                            .collectList();

                    Mono<Integer> totalMono = cartService.getTotal(userName);

                    Mono<Integer> balanceAmountMono = paymentApi.getBalance().map(Balance::getAmount);

                    return Mono.zip(itemsDtoMono, totalMono, balanceAmountMono)
                            .flatMap(tuple -> {
                                // Получаем результаты из кортежа
                                List<ItemDto> items = tuple.getT1();
                                Integer total = tuple.getT2();
                                Integer balance = tuple.getT3();

                                Map<String, Object> modelData = new HashMap<>();
                                modelData.put("items", items);
                                modelData.put("total", total);
                                modelData.put("balance", balance);
                                modelData.putAll(loginLoginAttributes);

                                return ServerResponse.ok().render("cart", modelData);
                            })
                            .switchIfEmpty(ServerResponse.notFound().build());
                });
    }

    public Mono<ServerResponse> addToCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return securityConfig.getUserNameMono()
                .flatMap(userName -> cartService.addToCart(productId, userName))
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> removeFromCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return securityConfig.getUserNameMono()
                .flatMap(userName -> cartService.removeFromCart(productId, userName))
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> setQuantity(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        Integer quantity = Integer.parseInt(request.pathVariable("quantity"));
        return securityConfig.getUserNameMono()
                .flatMap(userName -> cartService.setQuantity(productId, quantity, userName))
                .then(ServerResponse.seeOther(URI.create("/cart")).build());
    }

    public Mono<ServerResponse> makeOrder(ServerRequest request) {
        return securityConfig.getUserNameMono()
                .flatMap(userName -> cartService.getTotal(userName)
                        .flatMap(total -> {
                            if (total == 0) return ServerResponse.noContent().build();
                            else return paymentApi.makePayment(new PaymentRequest().amount(total));
                        })
                        .then(cartService.makeOrder(userName)))
                .then(ServerResponse.seeOther(URI.create("/order")).build());
    }
}
