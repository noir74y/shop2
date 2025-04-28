package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.services.OrderService;

@Component
@RequiredArgsConstructor
@Validated
public class OrderHandler {
    private final OrderService orderService;

    public Mono<ServerResponse> getAllOrders(ServerRequest request) {
        return Mono.empty();
//        return orderService.findAll()
//                .collectList()
//                .flatMap(orders -> {
//                    Map<String, Object> model = new HashMap<>();
//                    model.put("orders", orderMapper.bulkDomain2Dto(orders));
//                    model.put("total", orderService.getTotal().block());
//                    return ServerResponse.ok()
//                            .render("order-list", model);
//                });
    }

    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        var id = Long.parseLong(request.pathVariable("id"));
        return Mono.empty();

//        return orderService.findById(id)
//                .flatMap(order -> {
//                    OrderDto dto = orderMapper.domain2dto(order);
//                    Map<String, Object> model = new HashMap<>();
//                    model.put("items", dto.getItemsDto());
//                    model.put("total", dto.getTotal());
//                    return ServerResponse.ok()
//                            .render("order", model);
//                })
//                .onErrorResume(NumberFormatException.class,
//                        e -> ServerResponse.badRequest().build());
    }
}
