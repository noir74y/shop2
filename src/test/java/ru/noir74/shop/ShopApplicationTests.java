package ru.noir74.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.noir74.shop.models.domain.Item;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.entity.ItemEntity;
import ru.noir74.shop.models.entity.OrderEntity;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.repositories.ImageRepository;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.OrderRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

import java.util.List;

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

    @Autowired
    ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        imageRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        var product = productService.create(Product.builder().title("product1").price(1).description("desc").build());
        var item1 = Item.builder().product(product).quantity(1).build();
        var item2 = Item.builder().product(product).quantity(2).build();
        orderService.create(List.of(item1,item2));
    }
}

