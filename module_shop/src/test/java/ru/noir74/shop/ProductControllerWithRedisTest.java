package ru.noir74.shop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ProductControllerWithRedisTest extends GenericTest {
    private final ByteArrayOutputStream outputStreamToCatch = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        System.setOut(new PrintStream(outputStreamToCatch));
    }

    @AfterEach
    void reset() throws IOException {
        System.setOut(System.out);
    }

    @Test
    void getProductsPage_ShouldUseRedisCache() {
        checkForUsingRedisCacheForGetPage();

        createProduct();
        checkForUsingRedisCacheForGetPage();

        updateProduct();
        checkForUsingRedisCacheForGetPage();

        deleteProduct();
        checkForUsingRedisCacheForGetPage();
    }

    @Test
    void getProduct_ShouldUseRedisCache() {
        String outputFromMethod = "Fetching product from DB for ID: " + product.getId();

        outputStreamToCatch.reset();
        getProduct();
        assert outputStreamToCatch.toString().contains(outputFromMethod);

        outputStreamToCatch.reset();
        getProduct();
        assert !outputStreamToCatch.toString().contains(outputFromMethod);
    }

    private void checkForUsingRedisCacheForGetPage() {
        String outputFromGetPageMethod = "Fetching products page from DB";
        outputStreamToCatch.reset();
        getProductPage();
        assert outputStreamToCatch.toString().contains(outputFromGetPageMethod);
        outputStreamToCatch.reset();
        getProductPage();
        assert !outputStreamToCatch.toString().contains(outputFromGetPageMethod);
    }

    @WithMockUser(username = "test-user")
    private void getProductPage() {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk();
    }

    @WithMockUser(username = "test-user")
    private void getProduct() {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @WithMockUser(username = "test-user")
    private void createProduct() {
        isUserAuthenticated = true;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ClassPathResource("shlisselburg-krepost.jpeg"))
                .filename("shlisselburg-krepost.jpeg")
                .contentType(MediaType.IMAGE_JPEG);

        builder.part("title", "New Product");
        builder.part("price", "100");
        builder.part("description", "Description");

        webTestClient.post()
                .uri("/product/new/create")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange();
    }

    @WithMockUser(username = "test-user")
    private void updateProduct() {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        webTestClient.post()
                .uri("/product/" + product.getId() + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", "Updated Title")
                        .with("price", "200")
                        .with("description", "Updated Description")
                        .with("quantity", "3"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @WithMockUser(username = "test-user")
    private void deleteProduct() {
        isUserAuthenticated = true;

        cartRepository.deleteAll(testUserName).block();
        webTestClient.post()
                .uri("/product/" + product.getId() + "/delete")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}