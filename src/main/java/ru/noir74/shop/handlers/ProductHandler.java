package ru.noir74.shop.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.noir74.shop.configurations.AppConfiguration;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.dto.ProductDtoResp;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ProductService;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ProductHandler {
    private final AppConfiguration appConfiguration;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CartService cartService;

    public Mono<ServerResponse> getProductsPage(ServerRequest request) {
        String pageStr = request.queryParam("page").orElse("1");
        String sizeStr = request.queryParam("size").orElse("10");
        String sortStr = request.queryParam("sort").orElse("TITLE");

        int page = Integer.parseInt(pageStr);
        int size = Integer.parseInt(sizeStr);
        ProductSorting sorting = ProductSorting.valueOf(sortStr);

        return productService.getPage(page, size, sorting)
                .as(productMapper::fluxDomain2fluxDtoResp)
                .concatMap(productDtoResp -> cartService.getQuantityOfProduct(productDtoResp.getId())
                        .doOnNext(productDtoResp::setQuantity)
                        .thenReturn(productDtoResp))
                .collectList()
                .flatMap(productsWithQuantity ->
                        ServerResponse.ok()
                                .render("product-list",
                                        Rendering.view("product-list")
                                                .modelAttribute("page", pageStr)
                                                .modelAttribute("size", sizeStr)
                                                .modelAttribute("products", productsWithQuantity)
                                                .build()));
    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));

        return productService.get(id)
                .as(productMapper::monoDomain2monoDtoResp)
                .zipWith(cartService.getQuantityOfProduct(id))
                .flatMap(pair -> {
                    ProductDtoResp productDto = pair.getT1();
                    Integer quantity = pair.getT2();

                    return ServerResponse.ok()
                            .render("product",
                                    Rendering.view("product")
                                            .modelAttribute("id", productDto.getId())
                                            .modelAttribute("title", productDto.getTitle())
                                            .modelAttribute("price", productDto.getPrice())
                                            .modelAttribute("description", productDto.getDescription())
                                            .modelAttribute("quantity", quantity)
                                            .build());
                });
    }

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        var monoProductDtoReq = request.bodyToMono(ProductDtoReq.class);
        return monoProductDtoReq
                .as(productMapper::monoDtoReq2monoDomain)
                .transform(productService::create)
                .zipWith(monoProductDtoReq)
                .flatMap(pair -> {
                    var productId = pair.getT1().getId();
                    var productQuantity = pair.getT2().getQuantity();
                    if (productQuantity != 0) {
                        return cartService.addToCart(productId, productQuantity).then();
                    } else
                        return Mono.empty();
                })
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        var monoProductDtoReq = request.bodyToMono(ProductDtoReq.class);

        return monoProductDtoReq
                .as(productMapper::monoDtoReq2monoDomain)
                .transform(productService::update)
                .zipWith(monoProductDtoReq)
                .flatMap(pair -> {
                    var productId = pair.getT1().getId();
                    var productQuantity = pair.getT2().getQuantity();
                    if (productQuantity != 0)
                        return cartService.addToCart(productId, productQuantity).then();
                    else
                        return cartService.removeFromCart(productId).then();
                })
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return productService.delete(id)
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }

    public Mono<ServerResponse> addToCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return cartService.addToCart(productId)
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }

    public Mono<ServerResponse> removeFromCart(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        return cartService.removeFromCart(productId)
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }

    public Mono<ServerResponse> setQuantity(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable("productId"));
        Integer quantity = Integer.parseInt(request.pathVariable("quantity"));
        return cartService.setQuantity(productId, quantity)
                .then(ServerResponse.seeOther(URI.create("/product")).build());
    }
}

