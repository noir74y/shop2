package ru.noir74.shop;

import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
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

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class GenericTest {

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
                .price(3333)
                .description("description1")
                .file(new FilePartForTest("shlisselburg-krepost.jpeg"))
                .build())).block();
    }
}
