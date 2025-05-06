package ru.noir74.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.ProductService;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient

public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    @Transactional
    public void setUP() {
        productRepository.deleteAll().as(StepVerifier::create).verifyComplete();
    }

    @Test
    void getProductsPage_ShouldReturnOk() throws IOException {
        productService.save(Mono.just(Product.builder()
                .title("title1")
                .price(3333)
                .description("description1")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build())).block();

        productService.save(Mono.just(Product.builder()
                .title("title2")
                .price(3333)
                .description("description2")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build())).block();


        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null && responseBody.contains("<html") && responseBody.contains("products");
                    assert responseBody.contains("title1");
                    assert responseBody.contains("title2");
                });
    }

    @Test
    void getProduct_ShouldReturnProductPage() throws IOException {
        var product = productService.save(Mono.just(Product.builder()
                .title("title")
                .price(3333)
                .description("description")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build())).block();

        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("title");
                });
    }


    @Test
    void createProduct_WithFile_ShouldRedirect() throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ClassPathResource("shlisselburg-krepost.jpg"))
                .filename("shlisselburg-krepost.jpg")
                .contentType(MediaType.IMAGE_JPEG);

        builder.part("title", "New Product");
        builder.part("price", "100");
        builder.part("description", "Description");

        webTestClient.post()
                .uri("/product")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");
    }

    @Test
    void updateProduct_ShouldRedirect() throws IOException {
        var product = productService.save(Mono.just(Product.builder()
                .title("title")
                .price(3333)
                .description("description")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build())).block();

        Assertions.assertNotNull(product);
        webTestClient.post()
                .uri("/product/" + product.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", "Updated Title")
                        .with("price", "200")
                        .with("description", "Updated Description")
                        .with("quantity", "3"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

          productService.get(product.getId())
                .as(StepVerifier::create)
                .assertNext(obj -> {
                    assertThat(obj.getTitle()).isEqualTo("Updated Title");
                }).verifyComplete();

    }
}