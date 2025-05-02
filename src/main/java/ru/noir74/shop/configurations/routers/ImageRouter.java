package ru.noir74.shop.configurations.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.noir74.shop.handlers.ImageHandler;

@Configuration
public class ImageRouter {
    @Bean
    public RouterFunction<ServerResponse> faviconRoute(ImageHandler handler) {
        return RouterFunctions.route()
                .path("/image", builder -> builder
                        .GET("/{id}", handler::getImage)
                        .POST("/{id}", handler::setImage)
                ).build();
    }
}
