package ru.noir74.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.domain.Product;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.repositories.ProductRepository;
import ru.noir74.shop.services.ProductService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getPage(Integer page, Integer size, ItemSorting sort) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "title"));
        return productMapper.bulkEntity2domain(productRepository.findAll(pageable).stream().collect(Collectors.toCollection(LinkedList::new)));
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(Long id) {
        return productMapper.entity2domain(productRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public Product create(Product product) {
        return productMapper.entity2domain(productRepository.save(productMapper.domain2entity(product)));
    }

    @Override
    @Transactional
    public void update(Product product) {
        productRepository.save(productMapper.domain2entity(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
