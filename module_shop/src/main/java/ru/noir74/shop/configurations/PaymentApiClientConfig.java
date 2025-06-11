package ru.noir74.shop.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.noir74.shop.client.api.PaymentApi;
import ru.noir74.shop.client.clients.ApiClient;

@Configuration
public class PaymentApiClientConfig {
    @Value("${payment-service.base-url}")
    private String paymentServiceBaseUrl;

    @Bean
    public ApiClient paymentApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(paymentServiceBaseUrl);
        return apiClient;
    }

    @Bean
    public PaymentApi paymentApi(ApiClient paymentApiClient) {
        return new PaymentApi(paymentApiClient);
    }
}
