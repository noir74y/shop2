package ru.noir74.shop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ProductControllerWithRedisTest extends GenericTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void reset() throws IOException {
        System.setOut(System.out);
    }

    @Test
    void getProductsPage_ShouldUseRedisCache() {
        String outputFromMethod = "Fetching products page from DB: page=1, size=10, sort=TITLE";

        outputStreamCaptor.reset();
        getProductPage();
        assert outputStreamCaptor.toString().contains(outputFromMethod);

        outputStreamCaptor.reset();
        getProductPage();
        assert !outputStreamCaptor.toString().contains(outputFromMethod);
    }

    public void getProductPage() {
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
}