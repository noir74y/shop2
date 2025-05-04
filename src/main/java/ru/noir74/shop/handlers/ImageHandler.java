package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.services.ImageService;

@Component
@RequiredArgsConstructor
public class ImageHandler {
    private final ImageService imageService;

    public Mono<ServerResponse> getImage(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .flatMap(productId -> {
                    try {
                        return imageService.findImageById(Long.parseLong(productId))
                                .flatMap(image -> {
                                    MediaType mediaType = MediaType.parseMediaType("image/" + image.getImageType());
                                    return ServerResponse.ok().contentType(mediaType).bodyValue(image.getImage());
                                })
                                .switchIfEmpty(ServerResponse.notFound().build());
                    } catch (Exception e) {
                        return ServerResponse.badRequest().build();
                    }
                });
    }
}
