package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.misc.error.exceptions.NotFoundException;
import ru.noir74.shop.misc.error.exceptions.ProductIsUsedException;
import ru.noir74.shop.models.domain.Image;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final CartService cartService;
    private final ItemRepository itemRepository;


    @Override
    @Transactional(readOnly = true)
    public Flux<Product> getPage(Integer page, Integer size, ProductSorting sort) {
        return productRepository
                .findAllWithSortAndPagination(sort.name(), (long) size * (page - 1), size)
                .as(productMapper::fluxEntity2fluxDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Product> get(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("product is not found", "id=" + id)))
                .as(productMapper::monoEntity2monoDomain);
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return cartService.ifProductInCart(id)
                .flatMap(inCart -> {
                    if (inCart)
                        return Mono.error(new ProductIsUsedException("product is present in cart", "productId=" + id));
                    return itemRepository.isProductUsedInItems(id);
                })
                .flatMap(productIsUsedInItems -> {
                    if (productIsUsedInItems)
                        return Mono.error(new ProductIsUsedException("product is used in some order(s)", "productId=" + id));
                    return imageService.deleteById(id).then(productRepository.deleteById(id));
                });
    }

    public Mono<Product> save(Mono<Product> productMono) {
        return productMono
                .as(productMapper::monoDomain2monoEntity)
                .flatMap(productRepository::save)
                .as(productMapper::monoEntity2monoDomain)
                .zipWith(productMono)
                .flatMap(pair -> {
                            var savedProduct = pair.getT1();
                            var file = pair.getT2().getFile();
                            savedProduct.setFile(file);
                            return Product.isFileReadyToBeSaved(savedProduct.getFile())
                                    ? saveImage(savedProduct).thenReturn(savedProduct)
                                    : Mono.just(savedProduct);
                        }
                );
    }

    private Mono<Void> saveImage(Product product) {
        return Mono.fromSupplier(product::getFile)
                .flatMap(file -> DataBufferUtils.join(file.content())
                        .flatMap(dataBuffer ->
                                Mono.using(
                                        () -> dataBuffer,
                                        buffer -> {
                                            byte[] imageBytes = new byte[buffer.readableByteCount()];
                                            buffer.read(imageBytes);
                                            return imageService.setImage(
                                                    Image.builder()
                                                            .productId(product.getId())
                                                            .image(imageBytes)
                                                            .imageName(file.filename())
                                                            .build());
                                        },
                                        DataBufferUtils::release
                                )
                        )
                        .onErrorResume(e -> Mono.error(new RuntimeException("Failed to save image", e)))
                        .then());
    }
}
