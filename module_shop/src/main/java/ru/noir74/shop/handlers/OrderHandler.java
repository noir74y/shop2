package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.configurations.SecurityConfig;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.services.OrderService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Validated
public class OrderHandler {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final SecurityConfig securityConfig;

    public Mono<ServerResponse> getAllOrders(ServerRequest request) {
        Mono<Map<String, Object>> loginLoginAttributesMono = securityConfig.prepareLoginLogout();

        return orderService.findAll()
                .as(orderMapper::fluxDomain2fluxDto)
                .collectList()
                .zipWith(orderService.getTotal())
                .zipWith(loginLoginAttributesMono)
                .flatMap(tuple -> {
                    var tupleBox = tuple.getT1();
                    Map<String, Object> loginLoginAttributes = tuple.getT2();

                    var dtoList = tupleBox.getT1();
                    var total = tupleBox.getT2();

                    Map<String, Object> modelData = new HashMap<>();
                    modelData.put("orders", dtoList);
                    modelData.put("total", total);
                    modelData.putAll(loginLoginAttributes);

                    return ServerResponse.ok().render("order-list", modelData);
                });
    }

    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));
        Mono<Map<String, Object>> loginLoginAttributesMono = securityConfig.prepareLoginLogout();

        return orderService.findById(id)
                .as(orderMapper::monoDomain2monoDto)
                .zipWith(loginLoginAttributesMono)
                .flatMap(tuple -> {
                    var orderDto = tuple.getT1();
                    var loginLoginAttributes = tuple.getT2();

                    Map<String, Object> modelData = new HashMap<>();
                    modelData.put("items", orderDto.getItemsDto());
                    modelData.put("total", orderDto.getTotal());
                    modelData.putAll(loginLoginAttributes);

                    return ServerResponse.ok().render("order", modelData);
                });

    }
}
