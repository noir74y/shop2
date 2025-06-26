package ru.noir74.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Value("${spring.security.oauth2.client.provider.keycloak.jwk-set-uri}")
    private String JWK_SET_URI;

    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}")
    private String keyCloakAuthorizationUri;
    @Value("${spring.security.oauth2.realm}")
    private String keyCloakRealm;
    @Value("${spring.security.oauth2.client.id}")
    private String keyCloakClientId;
    @Value("${shop-service.base-url}")
    private String shopServiceBaseUrl;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        //.pathMatchers("/hello").hasRole("MANAGER")
                        .pathMatchers("/product").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth2Login -> {
                    oauth2Login.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/product"));
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcReactiveUserService() {
        final OidcReactiveOAuth2UserService oidcUserService = new OidcReactiveOAuth2UserService();

        return (oidcUserRequest) -> oidcUserService.loadUser(oidcUserRequest)
                .flatMap(oidcUser -> {
                    Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oidcUser.getAuthorities());
                    OAuth2AccessToken accessToken = oidcUserRequest.getAccessToken();

                    return jwtDecoder().decode(accessToken.getTokenValue())
                            .doOnError(e -> System.err.println("Ошибка при декодировании Access Token: " + e.getMessage()))
                            .map(decodedAccessToken -> {
                                Map<String, Object> accessTokenClaims = decodedAccessToken.getClaims();

                                if (accessTokenClaims.get("resource_access") instanceof Map<?, ?> resourceAccessInAccessToken) {
                                    if (resourceAccessInAccessToken.get("shop-application") instanceof Map<?, ?> clientAccessInAccessToken) {
                                        if (clientAccessInAccessToken.get("roles") instanceof Collection<?> clientRoles) {
                                            clientRoles.forEach(role -> {
                                                if (role instanceof String) {
                                                    mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + ((String) role).toUpperCase()));
                                                }
                                            });
                                        }
                                    }
                                }
                                return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
                            })
                            .onErrorResume(e -> Mono.just(new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo())));
                });
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(JWK_SET_URI).build();
    }

    public String getLogoutUrl(String usersIdToken) {
        var keycloakBaseUrl = keyCloakAuthorizationUri.substring(0, keyCloakAuthorizationUri.indexOf("/realms"));
        return String.format(
                "%s/realms/%s/protocol/openid-connect/logout?post_logout_redirect_uri=%s&id_token_hint=%s&client_id=%s",
                keycloakBaseUrl,
                keyCloakRealm,
                shopServiceBaseUrl,
                usersIdToken,
                keyCloakClientId
        );
    }
}