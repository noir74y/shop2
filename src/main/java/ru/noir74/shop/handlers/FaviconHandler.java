package ru.noir74.shop.handlers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FaviconHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(@NonNull ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.IMAGE_PNG)
                .bodyValue(new ClassPathResource("static/favicon.ico"));
    }
}
