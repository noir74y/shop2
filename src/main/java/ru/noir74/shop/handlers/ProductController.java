package ru.noir74.shop.handlers;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ProductService;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CartService cartService;

    @GetMapping
    public Mono<String> getProductsPage(Model model,
                                        @RequestParam(defaultValue = "1") String page,
                                        @RequestParam(defaultValue = "10") String size,
                                        @RequestParam(defaultValue = "TITLE") String sort) {

        log.info("Loading products page with params: page={}, size={}, sort={}", page, size, sort);

        int pageNum = Integer.parseInt(page);
        int sizeNum = Integer.parseInt(size);
        ProductSorting sorting = ProductSorting.valueOf(sort);

        return productService.getPage(pageNum, sizeNum, sorting)
                .transform(productMapper::fluxDomain2fluxDtoResp)
                .concatMap(productDtoResp ->
                        cartService.getQuantityOfProduct(productDtoResp.getId())
                                .doOnNext(productDtoResp::setQuantity)
                                .thenReturn(productDtoResp)
                )
                .collectList()
                .doOnNext(products -> {
                    model.addAttribute("page", page);
                    model.addAttribute("size", size);
                    model.addAttribute("products", products);
                })
                .thenReturn("product-list");
    }

    @GetMapping("{id}")
    public Mono<String> getProduct(Model model, @PathVariable("id") @NotNull @Positive Long id) {

        return productService.get(id)
                .transform(productMapper::monoDomain2monoDtoResp)
                .zipWith(cartService.getQuantityOfProduct(id))
                .doOnNext(pair -> {
                    ProductDtoResp productDto = pair.getT1();
                    Integer quantity = pair.getT2();

                    model.addAttribute("id", productDto.getId());
                    model.addAttribute("title", productDto.getTitle());
                    model.addAttribute("price", productDto.getPrice());
                    model.addAttribute("description", productDto.getDescription());
                    model.addAttribute("quantity", quantity);
                })
                .thenReturn("product");
    }

    @PostMapping
    public Mono<String> createProduct(@ModelAttribute ProductDtoReq productDtoReq) {
        return Mono.just(productDtoReq)
                .transform(productMapper::monoDtoReq2monoDomain)
                .transform(productService::save)
                .then(productDtoReq.getQuantity() != 0
                        ? cartService.addToCart(productDtoReq.getId(), productDtoReq.getQuantity())
                        : Mono.empty()
                )
                .thenReturn("redirect:/product");
    }

    @PostMapping("{id}")
    public Mono<String> updateProduct(@ModelAttribute ProductDtoReq productDtoReq, @PathVariable("id") @NotNull @Positive Long id) {
        return Mono.just(productDtoReq)
                .transform(productMapper::monoDtoReq2monoDomain)
                .transform(productService::save)
                .then(productDtoReq.getQuantity() != 0
                        ? cartService.addToCart(id, productDtoReq.getQuantity())
                        : cartService.removeFromCart(id)
                )
                .thenReturn("redirect:/product");
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public Mono<String> deleteProduct(@PathVariable("id") @NotNull @Positive Long id) {
        return productService.delete(id)
                .thenReturn("redirect:/product");
    }

    @PostMapping(value = "item/{productId}/add")
    public Mono<String> addToCart(@PathVariable("productId") Long productId) {
        return cartService.addToCart(productId)
                .thenReturn("redirect:/product");
    }

    @PostMapping(value = "item/{productId}/remove")
    public Mono<String> removeFromCart(@PathVariable("productId") @NotNull @Positive Long productId) {
        return cartService.removeFromCart(productId)
                .thenReturn("redirect:/product");
    }

    @PostMapping(value = "item/{productId}/quantity/{quantity}")
    public Mono<String> setQuantity(Model model,
                                    @PathVariable("productId") @NotNull @Positive Long productId,
                                    @PathVariable("quantity") @NotNull @Positive Integer quantity) {
        return cartService.setQuantity(productId, quantity)
                .thenReturn("redirect:/product");
    }
}

