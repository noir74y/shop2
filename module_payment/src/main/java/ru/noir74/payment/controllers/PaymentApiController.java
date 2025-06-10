package ru.noir74.payment.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;
import ru.noir74.payment.services.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentApiController implements PaymentApi {
    private final PaymentService paymentService;

    public Mono<ResponseEntity<PaymentConfirmation>> makePayment(
            @Valid @RequestBody Mono<PaymentRequest> paymentRequest,
            final ServerWebExchange exchange
    ) {
        return paymentService.makePayment(paymentRequest).map(ResponseEntity::ok);
    }
}
