package ru.noir74.shop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProductControllerTest extends GenericTest {
    private final ByteArrayOutputStream outputStreamToCatch = new ByteArrayOutputStream();

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        setUpGeneric();
        System.setOut(new PrintStream(outputStreamToCatch));
    }

    @AfterEach
    void reset() throws IOException {
        System.setOut(System.out);
    }

    @Test
    void getProductsPage_ShouldReturnOk_Anon_User() {
        isUserAuthenticated = false;
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null && responseBody.contains("<html") && responseBody.contains("products");
                    assertThat(responseBody).contains("title1");
                    assertThat(responseBody).doesNotContain("Действия");
                });
    }

    @Test
    @WithMockUser(username = "test-user")
    void getProductsPage_ShouldReturnOk_Auth_User() {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assert responseBody != null && responseBody.contains("<html") && responseBody.contains("products");
                    assertThat(responseBody).contains("title1");
                    assertThat(responseBody).contains("Действия");
                });
    }

    @Test
    void getProduct_ShouldReturnProductPage_Anon_User() throws IOException {
        isUserAuthenticated = false;
        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assertThat(responseBody).contains("title1");
                    assertThat(responseBody).doesNotContain("Сохранить");
                });
    }

    @Test
    @WithMockUser(username = "test-user")
    void getProduct_ShouldReturnProductPage_Auth_User() throws IOException {
        isUserAuthenticated = true;
        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assertThat(responseBody).contains("title1");
                    assertThat(responseBody).contains("Сохранить");
                });
    }

    @Test
    void createProduct_WithFile_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ClassPathResource("shlisselburg-krepost.jpeg"))
                .filename("shlisselburg-krepost.jpeg")
                .contentType(MediaType.IMAGE_JPEG);

        builder.part("title", "New Product");
        builder.part("price", "100");
        builder.part("description", "Description");

        webTestClient.post()
                .uri("/product/new/create")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }

    @Test
    @WithMockUser(username = "test-user")
    public void createProduct_WithFile_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ClassPathResource("shlisselburg-krepost.jpeg"))
                .filename("shlisselburg-krepost.jpeg")
                .contentType(MediaType.IMAGE_JPEG);

        builder.part("title", "New Product");
        builder.part("price", "100");
        builder.part("description", "Description");

        webTestClient.post()
                .uri("/product/new/create")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");
    }

    @Test
    void updateProduct_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        Assertions.assertNotNull(product);
        webTestClient.post()
                .uri("/product/" + product.getId() + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", "Updated Title")
                        .with("price", "200")
                        .with("description", "Updated Description")
                        .with("quantity", "3"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }

    @Test
    @WithMockUser(username = "test-user")
    void updateProduct_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;
        Assertions.assertNotNull(product);
        webTestClient.post()
                .uri("/product/" + product.getId() + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", "Updated Title")
                        .with("price", "200")
                        .with("description", "Updated Description")
                        .with("quantity", "3"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        productService.get(product.getId())
                .as(StepVerifier::create)
                .assertNext(obj -> {
                    assertThat(obj.getTitle()).isEqualTo("Updated Title");
                }).verifyComplete();

    }

    @Test
    void deleteProduct_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        webTestClient.post()
                .uri("/product/" + product.getId() + "/delete")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }


    @Test
    @WithMockUser(username = "test-user")
    void deleteProduct_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;
        webTestClient.post()
                .uri("/product/" + product.getId() + "/delete")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        StepVerifier.create(productService.get(product.getId()))
                .expectErrorMatches(throwable -> {
                    return throwable instanceof NotFoundException;
                })
                .verify();
    }

    @Test
    void addToCart_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }


    @Test
    @WithMockUser(username = "test-user")
    void addToCart_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    void removeFromCart_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/remove")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }

    @Test
    @WithMockUser(username = "test-user")
    void removeFromCart_ShouldRedirect_Auth_user() throws IOException {
        isUserAuthenticated = true;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/remove")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        StepVerifier.create(cartService.ifProductInCart(product.getId()))
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    @Test
    void setQuantityInCart_ShouldRedirect_Anon_User() throws IOException {
        isUserAuthenticated = false;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/oauth2/authorization/keycloak-user");
    }

    @Test
    @WithMockUser(username = "test-user")
    void setQuantityInCart_ShouldRedirect_Auth_User() throws IOException {
        isUserAuthenticated = true;
        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/add")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        webTestClient.post()
                .uri("/product/item/" + product.getId() + "/quantity/2")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/product");

        StepVerifier.create(cartService.findAll(testUserName))
                .assertNext(item -> assertThat(item.getQuantity()).isEqualTo(2)
                ).verifyComplete();

    }

}