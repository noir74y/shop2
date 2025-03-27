package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.configurations.AppConfiguration;
import ru.noir74.shop.misc.ItemSorting;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ProductService;

import java.util.Objects;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final AppConfiguration appConfiguration;
    private final ProductService productService;
    private final CartService cartService;
    private final ProductMapper productMapper;

    @GetMapping
    public String getPage(Model model,
                          @RequestParam(required = false, name = "page") String page,
                          @RequestParam(required = false, name = "size") String size,
                          @RequestParam(required = false, name = "sort") String sort) {
        var items = productMapper.bulkDomain2DtoResp(productService.getPage(
                Objects.nonNull(page) ? Integer.parseInt(page) : appConfiguration.getDefaultPageNumber(),
                Objects.nonNull(size) ? Integer.parseInt(size) : appConfiguration.getDefaultPageSize(),
                Objects.nonNull(sort) ? ItemSorting.valueOf(sort) : appConfiguration.getDefaultPageSorting()));
        return "items";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") Long id) {
        var item = productMapper.domain2dtoResp(productService.get(id));
        return "item";
    }

    @PostMapping
    public String create(@ModelAttribute ProductDtoReq productDtoReq) {
        productService.create(productMapper.dtoReq2domain(productDtoReq));
        return "redirect:/item";
    }

    @PostMapping(value = "{id}")
    public String update(@ModelAttribute ProductDtoReq productDtoReq, @PathVariable("id") Long id) {
        productService.update(productMapper.dtoReq2domain(productDtoReq));
        return "redirect:/item";
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/item";
    }

    @PostMapping(value = "{id}/addToCart")
    public String addToCart(@PathVariable("id") Long id) {
        cartService.addToCart(id);
        return "redirect:/cart";
    }

    @PostMapping(value = "{id}/removeFromCart")
    public String removeFromCart(@PathVariable("id") Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

}
