package ru.noir74.shop.configurations.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.noir74.shop.handlers.OrderHandler;

@Configuration
public class OrderRouter {
    @Bean
    public RouterFunction<ServerResponse> orderRoute(OrderHandler handler) {
        return RouterFunctions.route()
                .path("/order", builder -> builder
                        .GET("/{id}", handler::getOrderById)
                        .GET("", handler::getAllOrders)
                ).build();
    }
}
