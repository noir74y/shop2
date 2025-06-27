package ru.noir74.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

import java.util.*;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Value("${shop-service.base-url}")
    private String shopServiceBaseUrl;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        //.pathMatchers("/hello").hasRole("MANAGER")
                        .pathMatchers("/product/**", "/image/**", "static/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth2Login -> {
                    oauth2Login.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/product"));
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler logoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(this.clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri(shopServiceBaseUrl);
        return logoutSuccessHandler;
    }

    public Mono<String> prepareLoginLogout(String view, Model model) {
        return getAuthentification()
                .flatMap(authentication -> {
                    if (authentication instanceof OAuth2AuthenticationToken oauth2Token &&
                            oauth2Token.getPrincipal() instanceof OidcUser oidcUser) {
                        model.addAttribute("userName", oidcUser.getPreferredUsername());
                        model.addAttribute("logoutUrl", "/logout");
                    }
                    return Mono.just(view);
                })
                .switchIfEmpty(
                        clientRegistrationRepository.findByRegistrationId("keycloak-user")
                                .flatMap(clientRegistration -> {
                                    var loginUrl = "/oauth2/authorization/" + clientRegistration.getRegistrationId();
                                    model.addAttribute("loginUrl", loginUrl);
                                    return Mono.just(view);
                                })
                );
    }

    public Mono<Map<String, Object>> prepareLoginLogout() {
        Map<String, Object> attributes = new HashMap<>();
        return getAuthentification()
                .flatMap(authentication -> {
                    if (authentication instanceof OAuth2AuthenticationToken oauth2Token &&
                            oauth2Token.getPrincipal() instanceof OidcUser oidcUser) {
                        attributes.put("userName", oidcUser.getPreferredUsername());
                        attributes.put("logoutUrl", "/logout");
                    }
                    return Mono.just(attributes);
                })
                .switchIfEmpty(
                        clientRegistrationRepository.findByRegistrationId("keycloak-user")
                                .flatMap(clientRegistration -> {
                                    var loginUrl = "/oauth2/authorization/" + clientRegistration.getRegistrationId();
                                    attributes.put("loginUrl", loginUrl);
                                    return Mono.just(attributes);
                                })
                );
    }

    private Mono<Authentication> getAuthentification() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken);
    }
}