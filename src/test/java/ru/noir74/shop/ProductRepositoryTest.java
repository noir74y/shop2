package ru.noir74.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.entity.ProductEntity;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.ProductService;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void shouldGenerateIdOnSave() {
        ProductEntity product = new ProductEntity();
        product.setTitle("Test Product");
        product.setPrice(100);
        product.setDescription("Test Description");

        productRepository.save(product)
                .as(StepVerifier::create)
                .assertNext(savedProduct -> {
                    assertThat(savedProduct.getId()).isNotNull();
                    assertThat(savedProduct.getTitle()).isEqualTo("Test Product");
                })
                .verifyComplete();
    }

    @Test
    void t() throws IOException {
        Product product = Product.builder()
                .title("title")
                .price(3333)
                .description("description")
                .file(new FilePartForTest("shlisselburg-krepost.jpg"))
                .build();

        var productSaved = productService.save(Mono.just(product)).block();

//        var productSaved = productService.save(Mono.just(product))
//                .as(StepVerifier::create)
//                .assertNext(obj -> {
//                    assertThat(obj.getId()).isNotNull();
//                }).verifyComplete();

        System.out.println(productSaved);

    }
}