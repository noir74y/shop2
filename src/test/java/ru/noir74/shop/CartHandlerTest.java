package ru.noir74.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.models.mappers.helpers.OrderMapperHelper;
import ru.noir74.shop.repositories.CartRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient

public class CartHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderMapperHelper orderMapperHelper;

    private Product product;

    @BeforeEach
    @Transactional
    public void setUP() throws IOException {
        orderRepository.deleteAll().block();
        cartRepository.deleteAll().block();
        productRepository.deleteAll().block();

        product = productService.save(Mono.just(Product.builder()
                .title("title1")
                .price(3333)
                .description("description1")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build())).block();
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
                .transform(orderEntity -> orderMapper.fluxEntity2fluxDomain(orderEntity,orderMapperHelper))
                .blockFirst();

        Assertions.assertNotNull(order);
        StepVerifier.create(orderService.findAll())
                .expectNext(order)
                .expectComplete()
                .verify();
    }
}