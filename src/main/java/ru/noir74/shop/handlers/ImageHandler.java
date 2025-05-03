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

    public Mono<ServerResponse> setImage(ServerRequest request) {
        return ServerResponse.ok().build();
//        try {
//            var productId = Long.parseLong(request.pathVariable("id"));
//
//            Mono<FilePart> filePartMono = request.multipartData()
//                    .map(parts -> parts.getFirst("file"))
//                    .cast(FilePart.class);
//
//            return filePartMono.flatMap(filePart ->
//                            DataBufferUtils.join(filePart.content())
//                                    .flatMap(dataBuffer -> {
//                                        try {
//                                            byte[] imageBytes = new byte[dataBuffer.readableByteCount()];
//                                            dataBuffer.read(imageBytes);
//
//                                            Image image = Image.builder()
//                                                    .productId(productId)
//                                                    .image(imageBytes)
//                                                    .imageName(filePart.filename())
//                                                    .build();
//
//                                            return imageService.setImage(image)
//                                                    .then(ServerResponse.ok()
//                                                            .contentType(MediaType.TEXT_PLAIN)
//                                                            .bodyValue("product-list"));
//                                        } catch (Exception e) {
//                                            return ServerResponse.status(500)
//                                                    .bodyValue("Failed to read file: " + e.getMessage());
//                                        } finally {
//                                            DataBufferUtils.release(dataBuffer);
//                                        }
//                                    })
//                    )
//                    .switchIfEmpty(ServerResponse.badRequest()
//                            .bodyValue("File must be provided."));
//
//        } catch (Exception e) {
//            return ServerResponse.badRequest().build();
//        }
    }
}
