package ru.noir74.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openapitools.client.model.Balance;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.noir74.shop.client.api.PaymentApi;
import org.openapitools.client.model.PaymentRequest;

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
        when(paymentApi.getBalance()).thenReturn(Mono.just(new Balance().amount(1500)));
        when(paymentApi.makePayment(any(PaymentRequest.class))).thenReturn(Mono.empty());
    }

    @Test
    void viewCart_ShouldReturnOk() {
        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), 1).block();

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
    void addToCart_ShouldRedirect() {
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
    void removeFromCart_ShouldRedirect() throws IOException {
        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), 1).block();

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
    void setQuantityInCart_ShouldRedirect() throws IOException {
        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), 1).block();

        webTestClient.post()
                .uri("/cart/item/" + product.getId() + "/quantity/2")
                .exchange()
                .expectStatus().is3xxRedirection();

        StepVerifier.create(cartService.findAll())
                .assertNext(item -> assertThat(item.getQuantity()).isEqualTo(2)
                ).verifyComplete();

    }

    @Test
    void makeOrder_ShouldRedirect() throws IOException {
        Assertions.assertNotNull(product);
        cartService.addToCart(product.getId(), 1).block();

        webTestClient.post()
                .uri("/cart/order")
                .exchange()
                .expectStatus().is3xxRedirection();

        var order = orderRepository
                .findAll()
                .transform(orderEntity -> orderMapper.fluxEntity2fluxDomain(orderEntity, orderMapperHelper))
                .blockFirst();

        Assertions.assertNotNull(order);
        StepVerifier.create(orderService.findAll())
                .expectNext(order)
                .expectComplete()
                .verify();
    }
}