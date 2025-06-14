package ru.noir74.payment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.noir74.payment.models.dto.Balance;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;
import ru.noir74.payment.services.PaymentApiService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
@TestPropertySource(properties = {" ru.noir74.payment.services.PaymentApiService.currentBalance=valueForTest1"})
public class PaymentTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentApiService paymentApiService;

    @Value("${application.payment.initial-balance}")
    private int initialBalance;

    private int currentBalance;

    @BeforeEach
    void setUp() {
        paymentApiService.setCurrentBalance(initialBalance);
    }

    @Test
    void getBalanceTest() {
        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Balance.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    assertEquals(initialBalance, response.getResponseBody().getAmount());
                });
    }

    @Test
    void makePaymentTest() {
        var paymentAmount = 500;
        webTestClient.post()
                .uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new PaymentRequest().amount(paymentAmount)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentConfirmation.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    assertEquals(initialBalance - paymentAmount, response.getResponseBody().getNewBalance());
                });
    }
}
