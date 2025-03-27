package ru.noir74.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.repositories.ImageRepository;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

@SpringBootTest
class ShopApplicationTests {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    ImageService imageService;
    @Autowired
    CartService cartService;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        imageRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    void contextLoads() {
        var itemId = productService.create(Product.builder().title("product1").price(1).description("desc").build());
        //var orderId = orderService.create(List.of(OrderItem.builder().itemId(itemId).quantity(15).build()));
        //orderItemRepository.saveAll();
    }
}
