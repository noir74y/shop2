package ru.noir74.shop;

import org.springframework.boot.test.context.SpringBootTest;

//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.DisplayName.class)
//@SpringBootTest
class BDDTests {
//    @Autowired
//    private ItemMapper itemMapper;
//
//    @Test
//    void test() {
//        Item item = new Item();
//        ItemEntity itemEntity = new ItemEntity();
//        itemEntity = itemMapper.domainToEntity(item);
//    }
//}
//    @Autowired
//    ProductRepository productRepository;
//    @Autowired
//    OrderRepository orderRepository;
//    @Autowired
//    ImageRepository imageRepository;
//    @Autowired
//    ItemRepository itemRepository;
//    @Autowired
//    CartRepository cartRepository;
//
//    @Autowired
//    ProductService productService;
//    @Autowired
//    OrderService orderService;
//    @Autowired
//    ImageService imageService;
//    @Autowired
//    CartService cartService;
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @BeforeAll
//    void setUp() {
//        cartRepository.deleteAll();
//        itemRepository.deleteAll();
//        orderRepository.deleteAll();
//        imageRepository.deleteAll();
//        productRepository.deleteAll();
//    }
//
//    Product product;
//    Long productId;
//    Integer quantity;
//    MockMultipartFile mockMultipartFile;
//
//    @Test
//    @DisplayName("01 step - create product")
//    void createProduct() throws Exception {
//        product = Product.builder()
//                .title("product")
//                .price(1)
//                .description("description")
//                .build();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/product")
//                        .param("title", product.getTitle())
//                        .param("price", String.valueOf(product.getPrice()))
//                        .param("quantity", "1")
//                        .param("description", product.getDescription()))
//                .andExpect(status().is3xxRedirection());
//
//        product = productService.getPage(0, 10, ProductSorting.TITLE).getFirst();
//        productId = product.getId();
//        assertEquals(product, productService.get(productId));
//    }
//
//    @Test
//    @DisplayName("02 step - change product")
//    void changeProduct() throws Exception {
//        product = Product.builder().id(productId).title("product2").price(2).description("description2").build();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/" + productId)
//                        .param("title", product.getTitle())
//                        .param("price", String.valueOf(product.getPrice()))
//                        .param("description", product.getDescription()))
//                .andExpect(status().isOk());
//
//        assertEquals(product, productService.get(productId));
//    }
//
//    @Test
//    @DisplayName("03 step - set image for product")
//    void setImageForProduct() throws Exception {
//       mockMultipartFile = new MockMultipartFile(
//                "file",
//                "someFile.jpeg",
//                "image/jpeg",
//                "something".getBytes());
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/" + productId)
//                        .file(mockMultipartFile)
//                        .param("id", String.valueOf(productId))
//                )
//                .andExpect(status().isOk());
//
//        var image = imageService.findImageById(productId);
//        assertArrayEquals(mockMultipartFile.getBytes(), image.getImage());
//        assertEquals(mockMultipartFile.getOriginalFilename(), image.getImageName());
//    }
//
//    @Test
//    @DisplayName("04 step - set image for product again")
//    void setImageForProductAgain() throws Exception {
//        mockMultipartFile = new MockMultipartFile(
//                "file",
//                "someFile.jpeg",
//                "image/jpeg",
//                "fileContent".getBytes());
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/" + productId)
//                        .file(mockMultipartFile)
//                        .param("id", String.valueOf(productId))
//                )
//                .andExpect(status().isOk());
//
//        var image = imageService.findImageById(productId);
//        assertArrayEquals(mockMultipartFile.getBytes(), image.getImage());
//        assertEquals(mockMultipartFile.getOriginalFilename(), image.getImageName());
//    }
//
//    @Test
//    @DisplayName("05 step - get image for product")
//    void getImageForProduct() throws Exception {
//        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(
//                        MockMvcRequestBuilders.get("/image/" + productId))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//
//        assertEquals("fileContent", new String(mockHttpServletResponse.getContentAsByteArray()));
//    }
//
//    @Test
//    @DisplayName("06 step - try ot delete product which is in cart")
//    void tryToDeleteProductWhichIsInCart() throws Exception {
//        assertThrows(ProductIsUsedException.class, () -> productService.delete(productId));
//        cartRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("07 step - delete product")
//    void deleteProduct() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/" + productId)
//                        .param("_method", "delete")
//                        .param("id", String.valueOf(productId))
//                )
//                .andExpect(status().is3xxRedirection());
//        assertThrows(NotFoundException.class, () -> productService.get(productId));
//        assertThrows(NotFoundException.class, () -> imageService.findImageById(productId));
//    }
//
//    @Test
//    @DisplayName("08 step - add product again")
//    void addProductAgain() throws Exception {
//        createProduct();
//    }
//
//    @Test
//    @DisplayName("09 step - get product")
//    void getProduct() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/product/" + productId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("10 step - list products")
//    void listProducts() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/product")
//                        .param("size", "10")
//                        .param("sort", "TITLE"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("11 step - add product to cart")
//    void addProductToCart() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/cart/product/" + productId + "/add")
//                        .param("id", String.valueOf(productId)))
//                .andExpect(status().is3xxRedirection());
//
//        var item = cartService.findAll().getFirst();
//        assertEquals(1, item.getQuantity());
//        assertEquals(product, item.getProduct());
//    }
//
//    @Test
//    @DisplayName("12 step - remove product from cart")
//    void removeProductFromCart() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item/" + productId + "/remove")
//                        .param("id", String.valueOf(productId)))
//                .andExpect(status().is3xxRedirection());
//
//        assertEquals(0, cartService.findAll().size());
//    }
//
//    @Test
//    @DisplayName("13 step - add product to cart again")
//    void addProductToCartAgain() throws Exception {
//        addProductToCart();
//    }
//
//    @Test
//    @DisplayName("14 step - change quantity of product in cart")
//    void changeQuantityOfProductInCart() throws Exception {
//        quantity = 2;
//        mockMvc.perform(MockMvcRequestBuilders.post("/cart/item/" + productId + "/quantity/" + quantity)
//                .param("id", String.valueOf(productId))
//                .param("id", String.valueOf(quantity))
//        ).andExpect(status().is3xxRedirection());
//
//        assertEquals(quantity, cartService.findAll().getFirst().getQuantity());
//    }
//
//    @Test
//    @DisplayName("15 step - remove product from cart again")
//    void removeProductFromCartAgain() throws Exception {
//        quantity = 2;
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/item/" + productId + "/remove")
//                .param("id", String.valueOf(productId))
//        ).andExpect(status().is3xxRedirection());
//
//        assertEquals(0, cartService.findAll().size());
//    }
//
//    @Test
//    @DisplayName("16 step - add product to cart again2")
//    void addProductToCartAgain2() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/item/" + productId + "/add")
//                        .param("id", String.valueOf(productId)))
//                .andExpect(status().is3xxRedirection());
//
//        var item = cartService.findAll().getFirst();
//        assertEquals(1, item.getQuantity());
//        assertEquals(product, item.getProduct());
//    }
//
//    @Test
//    @DisplayName("17 step - change quantity of product in cart again")
//    void changeQuantityOfProductInCartAgain() throws Exception {
//        quantity = 2;
//        mockMvc.perform(MockMvcRequestBuilders.post("/product/item/" + productId + "/quantity/" + quantity)
//                .param("id", String.valueOf(productId))
//                .param("id", String.valueOf(quantity))
//        ).andExpect(status().is3xxRedirection());
//
//        assertEquals(quantity, cartService.findAll().getFirst().getQuantity());
//    }
//
//    @Test
//    @DisplayName("18 step - list cart")
//    void listCart() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("19 step - make order")
//    void makeOrder() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/cart/order"))
//                .andExpect(status().is3xxRedirection());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
//                .andExpect(status().isOk());
//
//        // TODO - переделать на реактивное использование
//        //assertEquals(1, orderService.findAll().size());
//    }
//
//    @Test
//    @DisplayName("20 step - list orders")
//    void listOrders() throws Exception {
//        // TODO - переделать на реактивное использование
//        //var orderId = orderService.findAll().getFirst().getId();
//        var orderId = 0;
//
//        // TODO переделать на реактивное использование
//        //var item = orderService.findById(orderId).getItems().getFirst();
//        var item = new Item();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId)
//                        .param("id", String.valueOf(orderId)))
//                .andExpect(status().isOk());
//
//        assertEquals(product, item.getProduct());
//        assertEquals(quantity, item.getQuantity());
//    }
//
//    @Test
//    @DisplayName("21 step - try to delete product")
//    void tryToDeleteProduct() {
//        assertThrows(ProductIsUsedException.class, () -> productService.delete(productId));
//    }
//}
}

