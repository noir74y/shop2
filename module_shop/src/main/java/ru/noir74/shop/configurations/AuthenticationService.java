package ru.noir74.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public Mono<String> prepareLoginLogout(Model model) {
        return getAuthenticationMono()
                .flatMap(authentication -> {
                    if (authentication instanceof OAuth2AuthenticationToken oauth2Token &&
                            oauth2Token.getPrincipal() instanceof OidcUser oidcUser) {
                        model.addAttribute("userName", oidcUser.getPreferredUsername());
                        model.addAttribute("logoutUrl", "/logout");
                    }
                    return Mono.just((String) Objects.requireNonNull(model.getAttribute("userName")));
                })
                .switchIfEmpty(
                        clientRegistrationRepository.findByRegistrationId("keycloak-user")
                                .flatMap(clientRegistration -> {
                                    var loginUrl = "/oauth2/authorization/" + clientRegistration.getRegistrationId();
                                    model.addAttribute("loginUrl", loginUrl);
                                    return Mono.just("");
                                })
                );
    }

    public Mono<Map<String, Object>> prepareLoginLogout() {
        Map<String, Object> attributes = new HashMap<>();
        return getAuthenticationMono()
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

//    public Mono<String> getUserNameMono() {
//        return getAuthentification().map(authentication -> ((OidcUser) authentication.getPrincipal()).getPreferredUsername());
//    }

    public Mono<String> getUserNameMono() {
        return getAuthenticationMono()
                .flatMap(authentication -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof OidcUser) {
                        return Mono.just(((OidcUser) principal).getPreferredUsername());
                    } else if (principal instanceof UserDetails) {
                        return Mono.just(((UserDetails) principal).getUsername());
                    } else if (principal != null) {
                        return Mono.just(principal.toString());
                    } else {
                        return Mono.error(new IllegalStateException("Principal is null"));
                    }
                })
                .switchIfEmpty(Mono.error(new IllegalStateException("Authentication is empty")));
    }

    private Mono<Authentication> getAuthenticationMono() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated);
        //.filter(authentication -> authentication instanceof OAuth2AuthenticationToken);
    }
}
