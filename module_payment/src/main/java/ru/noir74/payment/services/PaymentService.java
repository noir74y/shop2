package ru.noir74.payment.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.noir74.payment.models.dto.PaymentConfirmation;
import ru.noir74.payment.models.dto.PaymentRequest;

@Service
public class PaymentService {
    public Mono<PaymentConfirmation> processPayment(PaymentRequest request) {
        int currentBalance = 1500; // Условный текущий баланс
        int newBalance = currentBalance - request.getAmount();
        return Mono.just(new PaymentConfirmation().newBalance(newBalance));
    }
}