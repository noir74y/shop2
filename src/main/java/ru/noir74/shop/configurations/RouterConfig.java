package ru.noir74.shop.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.noir74.shop.handlers.FaviconHandler;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> faviconRoute(FaviconHandler handler) {
        return RouterFunctions.route()
                .GET("/test", handler)
                .GET("/favicon.ico", handler).build();
    }
}
