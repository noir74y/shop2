package ru.noir74.shop;

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
        //productRepository.deleteAll().block();
        productRepository.deleteAll().as(StepVerifier::create).verifyComplete();
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

//        Assertions.assertNotNull(product);
//        webTestClient.post()
//                .uri("/product/" + product.getId())
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromFormData("title", "Updated Title")
//                        .with("price", "200")
//                        .with("description", "Updated Description")
//                        .with("quantity", "3"))
//                .exchange()
//                .expectStatus().is3xxRedirection()
//                .expectHeader().location("/product");

//          productService.get(product.getId())
//                .as(StepVerifier::create)
//                .assertNext(obj -> {
//                    assertThat(obj.getTitle()).isEqualTo("Updated Title");
//                }).verifyComplete();

    }
}