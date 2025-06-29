package ru.noir74.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.Balance;
import org.openapitools.client.model.PaymentRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.noir74.shop.client.api.PaymentApi;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartHandlerTest extends GenericTest {

    @MockitoBean
    private PaymentApi paymentApi;

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        when(paymentApi.getBalance()).thenReturn(Mono.just(new Balance().amount(6000)));
        when(paymentApi.makePayment(any(PaymentRequest.class))).thenReturn(Mono.empty());
    }

    @Test
    @WithMockUser(username = "test-user")
    void viewCart_ShouldReturnOk_Auth_User() {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("title1");
                });
    }

    @Test
    @WithMockUser(username = "test-user")
    void addToCart_ShouldRedirect_Auth_User() {
        isUserAuthenticated = true;

        webTestClient.post()
                .uri("/cart/product/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    @WithMockUser(username = "test-user")
    void removeFromCart_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();

        webTestClient.post()
                .uri("/cart/item/" + product.getId() + "/remove")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    @Test
    @WithMockUser(username = "test-user")
    void setQuantityInCart_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();
        webTestClient.post()
                .uri("/cart/item/" + product.getId() + "/quantity/2")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.findAll(testUserName))
                .assertNext(item -> assertThat(item.getQuantity()).isEqualTo(2)
                ).verifyComplete();

    }

    @Test
    @WithMockUser(username = "test-user")
    void makeOrder_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();

        webTestClient.post()
                .uri("/cart/order")
                .exchange()
                .expectStatus().is3xxRedirection();

        var order = orderRepository
                .findAll()
                .transform(orderEntity -> orderMapper.fluxEntity2fluxDomain(orderEntity, orderMapperHelper))
                .blockFirst();

        Assertions.assertNotNull(order);
        StepVerifier.create(orderService.findAll(testUserName))
                .expectNext(order)
                .expectComplete()
                .verify();
    }

    @Test
    @WithMockUser(username = "test-user")
    void checkOrderOrderButtonWithGoodBalance_ShouldBeEnabled_Auth_User() {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("Заказать");
                });
    }

    @Test
    @WithMockUser(username = "test-user")
    void checkOrderOrderButtonWithBadBalance_ShouldBeDisabled_Auth_User() {
        isUserAuthenticated = true;

        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), testUserName).block();
        cartService.setQuantity(product.getId(), 10, testUserName).block();

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody.contains("Мало денег");
                });
    }
}