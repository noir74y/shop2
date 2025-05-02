package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.noir74.shop.models.domain.Image;
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
        try {
            var productId = Long.parseLong(request.pathVariable("id"));

            Mono<FilePart> filePartMono = request.multipartData()
                    .map(parts -> parts.getFirst("file"))
                    .cast(FilePart.class);

            return filePartMono.flatMap(filePart -> {
                try {
                    return filePart.content().collectList().flatMap(dataBuffers -> {
                        byte[] bytes = new byte[dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum()];
                        int offset = 0;
                        for (org.springframework.core.io.buffer.DataBuffer dataBuffer : dataBuffers) {
                            int length = dataBuffer.readableByteCount();
                            dataBuffer.read(bytes, offset, length);
                            offset += length;
                            org.springframework.core.io.buffer.DataBufferUtils.release(dataBuffer);
                        }
                        Image image = Image.builder()
                                .productId(productId)
                                .image(bytes)
                                .imageName(filePart.filename())
                                .build();
                        return imageService.setImage(image)
                                .then(ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("product-list"));
                    });
                } catch (Exception e) {
                    return ServerResponse.status(500).bodyValue("Failed to read file: " + e.getMessage());
                }
            }).switchIfEmpty(ServerResponse.badRequest().bodyValue("File must be provided."));

        } catch (Exception e) {
            return ServerResponse.badRequest().build();
        }
    }
}
