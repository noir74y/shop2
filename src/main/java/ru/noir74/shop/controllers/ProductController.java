package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.configurations.AppConfiguration;
import ru.noir74.shop.misc.ProductSorting;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.services.ProductService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final AppConfiguration appConfiguration;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public String getPage(Model model,
                          @RequestParam(required = false, name = "page") @Pattern(regexp = "^[1-9]+$") String page,
                          @RequestParam(required = false, name = "size") @Pattern(regexp = "10|20|50|100") String size,
                          @RequestParam(required = false, name = "sort") @Pattern(regexp = "^$") String sort) {
        var items = productMapper.bulkDomain2DtoResp(productService.getPage(
                Objects.nonNull(page) ? Integer.parseInt(page) - 1 : appConfiguration.getDefaultPageNumber() - 1,
                Objects.nonNull(size) ? Integer.parseInt(size) : appConfiguration.getDefaultPageSize(),
                Objects.nonNull(sort) ? ProductSorting.valueOf(sort) : appConfiguration.getDefaultPageSorting()));
        return "products";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") @NotEmpty @Pattern(regexp = "^[1-9]+$") Long id) {
        var item = productMapper.domain2dtoResp(productService.get(id));
        return "product";
    }

    @PostMapping
    public String create(@ModelAttribute ProductDtoReq productDtoReq) {
        productService.create(productMapper.dtoReq2domain(productDtoReq));
        return "redirect:/product";
    }

    @PostMapping(value = "{id}")
    public String update(@ModelAttribute ProductDtoReq productDtoReq, @PathVariable("id") @NotEmpty @Pattern(regexp = "^[1-9]+$") Long id) {
        productService.update(productMapper.dtoReq2domain(productDtoReq));
        return "redirect:/product";
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public String delete(@PathVariable("id") @NotEmpty @Pattern(regexp = "^[1-9]+$") Long id) {
        productService.delete(id);
        return "redirect:/product";
    }
}
