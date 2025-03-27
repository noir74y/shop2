package ru.noir74.shop.services;

import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> getPage(Integer page, Integer size, ItemSorting sort);

    Product get(Long id);

    Product create(Product product);

    void update(Product product);

    void delete(Long id);
}
