package ru.noir74.shop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class OrderHandlerTest extends GenericTest {
    private final ByteArrayOutputStream outputStreamToCatch = new ByteArrayOutputStream();

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        System.setOut(new PrintStream(outputStreamToCatch));

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();

        cartService.makeOrder(testUserName).block();

        order = orderRepository
                .findAll()
                .flatMap(orderEntity -> Mono.just(orderMapper.entity2domain(orderEntity)))
                .blockFirst();
    }

    @AfterEach
    void reset() throws IOException {
        System.setOut(System.out);
    }

    @Test
    void getAllOrders_ShouldReturnRedirect_Anon_User() {
        isUserAuthenticated = false;
        webTestClient.get()
                .uri("/order")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }

    @Test
    @WithMockUser(username = "test-user")
    void getAllOrders_ShouldReturnOk_Auth_User() {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/order")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("/order/" + order.getId());
                });
    }

    @Test
    void getOrderById_ShouldRedirect_Anon_User() {
        isUserAuthenticated = false;
        webTestClient.get()
                .uri("/order/" + order.getId())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }


    @Test
    @WithMockUser(username = "test-user")
    void getOrderById_ShouldReturnOk_Auth_User() {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/order/" + order.getId())
                .exchange()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("/product/" + product.getId());
                });
    }
}