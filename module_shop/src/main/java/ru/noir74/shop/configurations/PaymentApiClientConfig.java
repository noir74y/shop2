package ru.noir74.shop.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.noir74.shop.client.api.PaymentApi;
import ru.noir74.shop.client.clients.ApiClient;

import java.util.Collections;

@Configuration
public class PaymentApiClientConfig {
    @Value("${payment-service.base-url}")
    private String paymentServiceBaseUrl;

    private static final String CLIENT_CREDENTIALS_REGISTRATION_ID = "keycloak-payment-service";

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService
                );

        authorizedClientManager.setContextAttributesMapper(oauth2AuthorizeRequest -> {
            return Mono.just(Collections.emptyMap());
        });

        return authorizedClientManager;
    }

    @Bean(name = "paymentWebClient")
    public WebClient paymentWebClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        oauth2Client.setDefaultClientRegistrationId(CLIENT_CREDENTIALS_REGISTRATION_ID);

        return WebClient.builder()
                .baseUrl(paymentServiceBaseUrl)
                .filter(oauth2Client)
                .build();
    }

    @Bean
    public ApiClient paymentApiClient(WebClient paymentWebClient) {
        var apiClient = new ApiClient(paymentWebClient);
        apiClient.setBasePath(paymentServiceBaseUrl);
        return apiClient;
    }

    @Bean
    public PaymentApi paymentApi(ApiClient paymentApiClient) {
        return new PaymentApi(paymentApiClient);
    }
}
