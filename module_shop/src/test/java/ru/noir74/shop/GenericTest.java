package ru.noir74.shop;

import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import ru.noir74.shop.configurations.AuthenticationService;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.domain.Order;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.mappers.ImageMapper;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.models.mappers.helpers.OrderMapperHelper;
import ru.noir74.shop.repositories.CartRepository;
import ru.noir74.shop.repositories.ImageRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
public abstract class GenericTest {

//    @TestConfiguration
//    static class MockBeansConfig {
//        @Bean
//        @Primary
//        public AuthenticationService mockAuthenticationService() {
//            AuthenticationService mock = mock(AuthenticationService.class);
//
//            when(mock.prepareLoginLogout(any(Model.class)))
//                    .thenReturn(Mono.just("test-user"));
//
//            when(mock.getUserNameMono()).thenReturn(Mono.just("test-user"));
//            OidcIdToken oidcIdToken = mock(OidcIdToken.class);
//            when(oidcIdToken.getTokenValue()).thenReturn("mock-id-token-value");
//            when(oidcIdToken.getClaims()).thenReturn(Map.of(
//                    "sub", "test-user",
//                    "preferred_username", "test-user"
//            ));
//            Set<GrantedAuthority> authorities = new HashSet<>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//            OidcUser oidcUser = new DefaultOidcUser(authorities, oidcIdToken, "preferred_username");
//            OAuth2AuthenticationToken oauth2Token = new OAuth2AuthenticationToken(
//                    oidcUser,
//                    oidcUser.getAuthorities(),
//                    "keycloak-user");
//            when(mock.getAuthentificationMono()).thenReturn(Mono.just(oauth2Token));
//
//            return mock;
//        }
//    }

    @Container
    @ServiceConnection
    static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected CartRepository cartRepository;

    @Autowired
    protected CartService cartService;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderMapper orderMapper;

    @Autowired
    protected OrderMapperHelper orderMapperHelper;

    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    protected ImageMapper imageMapper;

    @MockitoSpyBean
    protected AuthenticationService authenticationService;
    protected boolean isUserAuthenticated;
    protected String testUserName = "test-user";

    protected Product product;
    protected Order order;
    protected Image image;

    @Transactional
    protected void setUpGeneric() throws IOException {
        orderRepository.deleteAll().block();
        cartRepository.deleteAll().block();
        productRepository.deleteAll().block();

        product = productService.create(Mono.just(Product.builder()
                .title("title1")
                .price(3000)
                .description("description1")
                .file(new FilePartTestUtilities("shlisselburg-krepost.jpeg"))
                .build())).block();

        doAnswer(invocation -> {
            System.out.println("DEBUG: Intercepted Model in prepareLoginLogout. Added userName and loggedIn.");
            if (isUserAuthenticated) {
                Model model = invocation.getArgument(0);
                model.addAttribute("userName", testUserName);
                return Mono.just(testUserName);
            } else
                return Mono.just("");
        }).when(authenticationService).prepareLoginLogout(any(Model.class));

        doAnswer(invocation -> {
            Map<String, Object> attributes = new HashMap<>();
            if (this.isUserAuthenticated)
                attributes.put("userName", testUserName);
            return Mono.just(attributes);
        }).when(authenticationService).prepareLoginLogout();

        doAnswer(invocation -> {
            if (this.isUserAuthenticated)
                return Mono.just(testUserName);
            else
                return Mono.error(new IllegalStateException("Authentication is empty"));
        }).when(authenticationService).getUserNameMono();


    }
}
