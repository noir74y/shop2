package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.noir74.shop.repositories.ItemRepository;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;
import ru.noir74.shop.services.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final CartService cartService;


//    @Override
//    @Transactional(readOnly = true)
//    public List<Product> getPage(Integer page, Integer size, ProductSorting sort) {
//        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "title"));
//        return productMapper.bulkEntity2domain(productRepository
//                .findAll(pageable)
//                .stream()
//                .collect(Collectors.toCollection(LinkedList::new)));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Product get(Long id) {
//        return productMapper.entity2domain(productRepository.findById(id).orElseThrow(() -> new NotFoundException("product is not found", "id=" + id)));
//    }
//
//    @Override
//    @Transactional
//    public Product create(Product product) throws IOException {
//        return save(product);
//    }
//
//    @Override
//    @Transactional
//    public void update(Product product) throws IOException {
//        save(product);
//    }
//
//    @Override
//    @Transactional
//    public void delete(Long id) {
//        if (cartService.ifProductInCart(id)) {
//            throw new ProductIsUsedException("product is present in cart", "productId=" + id);
//        } else if (Optional.ofNullable(itemRepository.isProductUsesInItems(id)).isPresent()) {
//            throw new ProductIsUsedException("product is used in some order(s)", "productId=" + id);
//        } else {
//            imageService.deleteById(id);
//            productRepository.deleteById(id);
//        }
//    }
//
//    @Transactional
//    private Product save(Product product) throws IOException {
//        MultipartFile file = product.getFile();
//        product = productMapper.entity2domain(productRepository.save(productMapper.domain2entity(product)));
//        saveImage(product.getId(), file);
//        return product;
//    }
//
//    @Transactional
//    private void saveImage(Long productId, MultipartFile file) throws IOException {
//        if (Objects.nonNull(file)) {
//            var image = Image.builder()
//                    .productId(productId)
//                    .image(file.getBytes())
//                    .imageName(file.getOriginalFilename()).build();
//            if (image.isImageReadyToBeSaved()) {
//                image.setProductId(productId);
//                imageService.setImage(image);
//            }
//        }
//    }

}
