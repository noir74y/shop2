package ru.noir74.shop;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
import ru.noir74.shop.misc.error.exceptions.ProductIsUsedException;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.repositories.*;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.OrderService;
import ru.noir74.shop.services.ProductService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class BDDTests {
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

    Product product;
    Long productId;
    Integer quantity;

    @Test
    @DisplayName("01 step - create product")
    void createProduct() throws Exception {
        product = Product.builder().title("product").price(1).description("description").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .param("title", product.getTitle())
                        .param("price", String.valueOf(product.getPrice()))
                        .param("description", product.getDescription()))
                .andExpect(status().is3xxRedirection());

        product = productService.getPage(0, 10, ProductSorting.TITLE).getFirst();
        productId = product.getId();
        assertEquals(product, productService.get(productId));
    }

    @Test
    @DisplayName("02 step - change product")
    void changeProduct() throws Exception {
        product = Product.builder().id(productId).title("product2").price(2).description("description2").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/product/" + productId)
                        .param("title", product.getTitle())
                        .param("price", String.valueOf(product.getPrice()))
                        .param("description", product.getDescription()))
                .andExpect(status().is3xxRedirection());

        assertEquals(product, productService.get(productId));
    }

    @Test
    @DisplayName("03 step - set image for product")
    void setImageForProduct() throws Exception {
        var mockMultipartFile = new MockMultipartFile(
                "file",
                "someFile.jpeg",
                "image/jpeg",
                new byte[]{(byte) 0x00});

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/" + productId)
                        .file(mockMultipartFile)
                        .param("id", String.valueOf(productId))
                )
                .andExpect(status().isOk());

        var image = imageService.findImageById(productId);
        assertArrayEquals(mockMultipartFile.getBytes(), image.getImage());
        assertEquals(mockMultipartFile.getOriginalFilename(), image.getImageName());
    }

    @Test
    @DisplayName("04 step - delete product")
    void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/product/" + productId)
                        .param("_method", "delete")
                        .param("id", String.valueOf(productId))
                )
                .andExpect(status().is3xxRedirection());

        assertThrows(NotFoundException.class, () -> productService.get(productId));
        assertThrows(NotFoundException.class, () -> imageService.findImageById(productId));
    }

    @Test
    @DisplayName("05 step - add product again")
    void addProductAgain() throws Exception {
        createProduct();
    }

    @Test
    @DisplayName("06 step - get product")
    void getProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/" + productId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("07 step - list products")
    void listProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product")
                        .param("size", "10")
                        .param("sort", "TITLE"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("08 step - add product to cart")
    void addProductToCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/product/" + productId + "/add")
                        .param("id", String.valueOf(productId)))
                .andExpect(status().is3xxRedirection());

        var item = cartService.findAll().getFirst();
        assertEquals(1, item.getQuantity());
        assertEquals(product, item.getProduct());
    }

    @Test
    @DisplayName("09 step - remove product from cart")
    void removeProductFromCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item/" + productId + "/remove")
                        .param("id", String.valueOf(productId)))
                .andExpect(status().is3xxRedirection());

        assertEquals(0, cartService.findAll().size());
    }

    @Test
    @DisplayName("10 step - add product to cart again")
    void addProductToCartAgain() throws Exception {
        addProductToCart();
    }

    @Test
    @DisplayName("11 step - change quantity of product in cart")
    void changeQuantityOfProductInCart() throws Exception {
        quantity = 2;
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item/" + productId + "/quantity/" + quantity)
                .param("id", String.valueOf(productId))
                .param("id", String.valueOf(quantity))
        ).andExpect(status().is3xxRedirection());

        assertEquals(quantity, cartService.findAll().getFirst().getQuantity());
    }

    @Test
    @DisplayName("12th step - list cart")
    void listCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("13th step - make order")
    void makeOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/order"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(status().isOk());

        assertEquals(1, orderService.getAll().size());
    }

    @Test
    @DisplayName("14th step - list orders")
    void listOrders() throws Exception {
        var orderId = orderService.getAll().getFirst().getId();
        var item = orderService.get(orderId).getItems().getFirst();

        mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId)
                        .param("id", String.valueOf(orderId)))
                .andExpect(status().isOk());

        assertEquals(product, item.getProduct());
        assertEquals(quantity, item.getQuantity());
    }

    @Test
    @DisplayName("15th step - try to delete product")
    void tryToDeleteProduct() {
        assertThrows(ProductIsUsedException.class, () -> productService.delete(productId));
    }
}


