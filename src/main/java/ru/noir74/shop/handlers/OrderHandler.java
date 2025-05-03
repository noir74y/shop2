package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.services.OrderService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Validated
public class OrderHandler {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public Mono<ServerResponse> getAllOrders(ServerRequest request) {
        return orderService.findAll()
                .as(orderMapper::fluxDomain2fluxDto)
                .collectList()
                .zipWith(orderService.getTotal())
                .flatMap(tuple -> {
                    var dtoList = tuple.getT1();
                    var total = tuple.getT2();

                    return ServerResponse.ok()
                            .render("order-list",
                                    Map.of("orders", dtoList, "total", total));
                });
    }

    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));

        return orderService.findById(id)
                .as(orderMapper::monoDomain2monoDto)
                .flatMap(dto ->
                        ServerResponse.ok()
                                .render("order",
                                        Map.of("items", dto.getItemsDto(), "total", dto.getTotal())));

    }
}
