package ru.noir74.payment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.noir74.payment.models.dto.Balance;
import ru.noir74.payment.services.PaymentService;

@RestController
@RequiredArgsConstructor
public class BalanceApiController implements BalanceApi {
    private final PaymentService paymentService;

    @Override
    public Mono<ResponseEntity<Balance>> getBalance(final ServerWebExchange exchange) {
        return paymentService.getBalance().map(ResponseEntity::ok);
    }
}
