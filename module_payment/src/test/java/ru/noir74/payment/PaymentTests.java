package ru.noir74.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.noir74.payment.configurations.ResourceServerConfig;
import ru.noir74.payment.controllers.PaymentApiController;
import ru.noir74.payment.models.dto.Balance;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;
import ru.noir74.payment.services.PaymentApiService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PaymentApiController.class)
@Import(ResourceServerConfig.class)
public class PaymentTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PaymentApiService paymentApiService;

    @Value("${application.payment.initial-balance:1500}")
    private int initialBalance;

    @Test
    void getBalanceTest_Unauthorized_ShouldReturn401() {
        webTestClient.get()
                .uri("/balance")
                .exchange() // Отправляем запрос
                .expectStatus().isUnauthorized();
    }

    @Test
    void getBalanceTest_Authorized_ShouldReturnBalance() {
        when(paymentApiService.getBalance()).thenReturn(Mono.just(new Balance().amount(initialBalance)));
        webTestClient.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .get()
                .uri("/balance")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Balance.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(initialBalance, response.getResponseBody().getAmount());
                });
    }

    @Test
    void makePaymentTest_Unauthorized_ShouldReturn401() {
        var paymentAmount = 500;
        webTestClient.post()
                .uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new PaymentRequest().amount(paymentAmount)))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void makePaymentTest_Authorized_ShouldReturnPaymentConfirmation() {
        var paymentAmount = 500;
        var expectedNewBalance = initialBalance - paymentAmount;

        when(paymentApiService.makePayment(any(Mono.class)))
                .thenReturn(Mono.just(new PaymentConfirmation().newBalance(expectedNewBalance)));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post()
                .uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new PaymentRequest().amount(paymentAmount)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentConfirmation.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(expectedNewBalance, response.getResponseBody().getNewBalance());
                });
    }
}
