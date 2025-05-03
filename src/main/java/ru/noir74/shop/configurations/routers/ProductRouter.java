package ru.noir74.shop.configurations.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.noir74.shop.handlers.ProductHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        return route()
                .path("/product_test", builder1 -> builder1
                        .path("/{id}", builder2 -> builder2
                                .GET("", productHandler::getProduct)
                                .POST("", productHandler::updateProduct)
                                .POST("", RequestPredicates.queryParam("_method", "delete"::equalsIgnoreCase), productHandler::deleteProduct)
                        )
                        .path("/item/{productId}", builder3 -> builder3
                                .POST("/add", productHandler::addToCart)
                                .POST("/remove", productHandler::removeFromCart)
                                .POST("/quantity/{quantity}", productHandler::setQuantity)
                        )
                        .GET("", productHandler::getProductsPage)
                        .POST("", productHandler::createProduct)
                ).build();
    }
}
