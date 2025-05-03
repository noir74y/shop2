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
    public Mono<Product> create(Mono<Product> product) {
        return save(product);
    }

    @Override
    @Transactional
    public Mono<Product> update(Mono<Product> product) {
        return save(product);
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

    private Mono<Product> save(Mono<Product> product) {
        return productMapper.monoDomain2monoEntity(product)
                .flatMap(productRepository::save)
                .as(productMapper::monoEntity2monoDomain)
                .flatMap(savedProduct ->
                        saveImage(savedProduct)
                                .then(Mono.just(savedProduct))
                );
    }

    private Mono<Void> saveImage(Product product) {
        return Mono.justOrEmpty(product.getFile())
                .flatMap(filePart -> DataBufferUtils.join(filePart.content())
                        .flatMap(dataBuffer -> {
                            try {
                                byte[] imageBytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(imageBytes);
                                DataBufferUtils.release(dataBuffer);

                                Image image = Image.builder()
                                        .productId(product.getId())
                                        .image(imageBytes)
                                        .imageName(filePart.filename())
                                        .build();

                                return image.isImageReadyToBeSaved()
                                        ? imageService.setImage(image)
                                        : Mono.empty();
                            } catch (Exception e) {
                                DataBufferUtils.release(dataBuffer); // Освобождаем в случае ошибки
                                return Mono.error(new RuntimeException("Failed to save image", e));
                            } finally {
                                DataBufferUtils.release(dataBuffer);
                            }
                        })
                );
    }
}
