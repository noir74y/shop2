package ru.noir74.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.contains;

@SpringBootTest
@AutoConfigureWebTestClient
public class ImageHandlerTest extends GenericTest {

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        image = imageRepository.findAll().flatMap(imageEntity -> Mono.just(imageMapper.entity2domain(imageEntity))).blockFirst();
    }

    @Test
    void getOrder_ShouldReturnOk() {
        webTestClient.get()
                .uri("/image/{id}", image.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.IMAGE_JPEG)
                .expectBody(byte[].class).isEqualTo(image.getImage());
    }
}