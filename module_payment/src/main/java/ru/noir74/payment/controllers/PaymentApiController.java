package ru.noir74.payment.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.noir74.payment.models.dto.Balance;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;
import ru.noir74.payment.services.PaymentApiService;

@RestController
@RequiredArgsConstructor
public class PaymentApiController implements PaymentApi {
    private final PaymentApiService paymentApiService;

    @Override
    public Mono<ResponseEntity<Balance>> getBalance(final ServerWebExchange exchange) {
        return paymentApiService.getBalance().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<PaymentConfirmation>> makePayment(
            @Valid @RequestBody Mono<PaymentRequest> paymentRequest,
            final ServerWebExchange exchange
    ) {
        return paymentApiService.makePayment(paymentRequest).map(ResponseEntity::ok);
    }
}
