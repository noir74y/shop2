package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.configurations.AuthenticationService;
import ru.noir74.shop.configurations.SecurityConfig;
import ru.noir74.shop.models.dto.OrderDto;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.services.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Validated
public class OrderHandler {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final AuthenticationService securityConfig;

    public Mono<ServerResponse> getAllOrders(ServerRequest request) {
        Mono<Map<String, Object>> loginLogoutAttributesMono = securityConfig.prepareLoginLogout();

        return loginLogoutAttributesMono
                .flatMap(loginLoginAttributes -> {
                    String username = (String) loginLoginAttributes.getOrDefault("userName", "");

                    return orderService.findAll(username)
                            .as(orderMapper::fluxDomain2fluxDto)
                            .collectList()
                            .zipWith(orderService.getTotal(username))
                            .flatMap(tuple -> {
                                List<OrderDto> dtoList = tuple.getT1();
                                Integer total = tuple.getT2();

                                Map<String, Object> modelData = new HashMap<>();
                                modelData.put("orders", dtoList);
                                modelData.put("total", total);
                                modelData.putAll(loginLoginAttributes);

                                return ServerResponse.ok().render("order-list", modelData);
                            })
                            .switchIfEmpty(ServerResponse.notFound().build());
                });
    }

    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        Mono<Map<String, Object>> loginLogoutAttributesMono = securityConfig.prepareLoginLogout();
        var id = Long.parseLong(request.pathVariable("id"));

        return loginLogoutAttributesMono
                .flatMap(loginLogoutAttributes -> {
                    return orderService.findById(id)
                            .as(orderMapper::monoDomain2monoDto)
                            .flatMap(orderDto -> {
                                Map<String, Object> modelData = new HashMap<>();
                                modelData.put("items", orderDto.getItemsDto());
                                modelData.put("total", orderDto.getTotal());
                                modelData.putAll(loginLogoutAttributes);
                                return ServerResponse.ok().render("order", modelData);
                            })
                            .switchIfEmpty(ServerResponse.notFound().build());
                });
    }
}
