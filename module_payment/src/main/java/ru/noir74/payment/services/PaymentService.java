package ru.noir74.payment.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.noir74.payment.models.dto.Balance;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;

@Service
public class PaymentService {
    @Value("${application.payment.initial-balance}")
    private int currentBalance;

    public Mono<PaymentConfirmation> makePayment(Mono<PaymentRequest> requestMono) {
        return requestMono.flatMap(request -> {
            currentBalance -= request.getAmount(); // эмуляция списания средств
            return Mono.just(new PaymentConfirmation().newBalance(currentBalance));
        });
    }

    public Mono<Balance> getBalance() {
        return Mono.just(new Balance(currentBalance));
    }
}