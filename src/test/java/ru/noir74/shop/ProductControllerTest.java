package ru.noir74.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient

public class ProductControllerTest extends GenericTest {
    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
    }

    @Test
    void getProductsPage_ShouldReturnOk() {
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null && responseBody.contains("<html") && responseBody.contains("products");
                    assert responseBody.contains("title1");
                });
    }

    @Test
    void getProduct_ShouldReturnProductPage() throws IOException {
        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("title1");
                });
    }


    @Test
    void createProduct_WithFile_ShouldRedirect() throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ClassPathResource("shlisselburg-krepost.jpeg"))
                .filename("shlisselburg-krepost.jpeg")
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

    @Test
    void deleteProduct_ShouldRedirect() throws IOException {
        webTestClient.post()
                .uri("/product/" + product.getId() + "/delete")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        StepVerifier.create(productService.get(product.getId()))
                .expectErrorMatches(throwable -> {
                    return throwable instanceof NotFoundException;
                })
                .verify();
    }

    @Test
    void addToCart_ShouldRedirect() throws IOException {
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    void removeFromCart_ShouldRedirect() throws IOException {
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/remove")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(false)
                .expectComplete()
                .verify();
    }


    @Test
    void setQuantityInCart_ShouldRedirect() throws IOException {
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/quantity/2")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.findAll())
                .assertNext(item -> assertThat(item.getQuantity()).isEqualTo(2)
                ).verifyComplete();

    }

}