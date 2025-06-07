package ru.noir74.shop.configurations.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.noir74.shop.handlers.CartHandler;

@Configuration
public class CartRouter {
    @Bean
    public RouterFunction<ServerResponse> cartRoutes(CartHandler cartHandler) {
        return RouterFunctions.route()
                .path("/cart", builder -> builder
                        .GET("", cartHandler::viewCart)
                        .POST("/product/{productId}/add", cartHandler::addToCart)
                        .POST("/item/{productId}/remove", cartHandler::removeFromCart)
                        .POST("/item/{productId}/quantity/{quantity}", cartHandler::setQuantity)
                        .POST("/order", cartHandler::makeOrder)
                ).build();
    }
}
