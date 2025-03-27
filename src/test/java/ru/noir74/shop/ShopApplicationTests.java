package ru.noir74.shop;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.noir74.shop.misc.ProductSorting;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.repositories.*;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
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
    CartRepository cartRepository;

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    ImageService imageService;
    @Autowired
    CartService cartService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    void setUp() {
        cartRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        imageRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("01 - create product")
    void createProduct() throws Exception {
        var product = Product.builder().title("product").price(1).description("description").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .param("title", product.getTitle())
                        .param("price", String.valueOf(product.getPrice()))
                        .param("description",product.getDescription()))
                .andExpect(status().is3xxRedirection());

        var productId = productService.getPage(0,10, ProductSorting.TITLE).getFirst().getId();
        product.setId(productId);
        assertEquals(product, productService.get(productId));
    }

    @Test
    @DisplayName("02 - change product")
    void changeProduct(){

    }

    @Test
    @DisplayName("03 - set image for product")
    void setImageForProduct(){

    }

    @Test
    @DisplayName("04 - delete product")
    void deleteProduct(){

    }

    @Test
    @DisplayName("05 - add product again")
    void addProductAgain(){

    }

    @Test
    @DisplayName("06 - add product to cart")
    void addProductToCart() {

    }

    @Test
    @DisplayName("07 - remove product from cart")
    void removeProductFromCart() {

    }

    @Test
    @DisplayName("08 - add product to cart again")
    void addProductToCartAgain() {

    }

    @Test
    @DisplayName("09 - change quantity of product in cart")
    void changeQuantityOfProductInCart() {

    }

    @Test
    @DisplayName("10th - make order")
    void makeOrder() {

    }

    @Test
    @DisplayName("11th - list orders")
    void listOrders() {

    }

}


