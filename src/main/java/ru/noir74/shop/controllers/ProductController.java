package ru.noir74.shop.controllers;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.configurations.AppConfiguration;
import ru.noir74.shop.misc.enums.ProductSorting;
import ru.noir74.shop.misc.validators.ValueOfEnumConstraint;
import ru.noir74.shop.models.dto.ProductDtoReq;
import ru.noir74.shop.models.mappers.ProductMapper;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ProductService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final AppConfiguration appConfiguration;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CartService cartService;

    @GetMapping
    public String getPage(Model model,
                          @RequestParam(defaultValue = "1", name = "page") @Pattern(regexp = "^([1-9]+)|$") String page,
                          @RequestParam(required = false, name = "size") @Pattern(regexp = "10|20|50|100") String size,
                          @RequestParam(required = false, name = "sort") @ValueOfEnumConstraint(enumClass = ProductSorting.class) String sort) {
        var products = productMapper.bulkDomain2DtoResp(productService.getPage(
                Objects.nonNull(page) ? Integer.parseInt(page) - 1 : appConfiguration.getDefaultPageNumber() - 1,
                Objects.nonNull(size) ? Integer.parseInt(size) : appConfiguration.getDefaultPageSize(),
                Objects.nonNull(sort) ? ProductSorting.valueOf(sort) : appConfiguration.getDefaultPageSorting()));

        products.forEach(productDtoResp -> productDtoResp.setQuantity(cartService.getQuantityOfProduct(productDtoResp.getId())));

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("products", products);

        return "product-list";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") @NotEmpty @Positive Long id) {
        var product = productMapper.domain2dtoResp(productService.get(id));

        model.addAttribute("id", product.getId());
        model.addAttribute("title", product.getTitle());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("description", product.getDescription());
        model.addAttribute("quantity", cartService.getQuantityOfProduct(id));

        return "product";
    }

    @PostMapping
    public String create(@ModelAttribute ProductDtoReq productDtoReq) throws IOException {
        var product = productService.create(productMapper.dtoReq2domain(productDtoReq));
        if (productDtoReq.getQuantity() != 0) cartService.addToCart(product.getId(), productDtoReq.getQuantity());
        return "redirect:/product";
    }

    @PostMapping(value = "{id}")
    public String update(@ModelAttribute ProductDtoReq productDtoReq, @PathVariable("id") @NotEmpty @Positive Long id) throws IOException {
        productService.update(productMapper.dtoReq2domain(productDtoReq));
        if (productDtoReq.getQuantity() != 0) cartService.addToCart(productDtoReq.getId(), productDtoReq.getQuantity());
        else cartService.removeFromCart(productDtoReq.getId());
        return "redirect:/product";
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public String delete(@PathVariable("id") @NotEmpty @Positive Long id) {
        productService.delete(id);
        return "redirect:/product";
    }

    @PostMapping(value = "item/{id}/add")
    public String addToCart(@PathVariable("id") @NotEmpty @Positive Long productId) {
        cartService.addToCart(productId);
        return "redirect:/product";
    }

    @PostMapping(value = "item/{id}/remove")
    public String removeFromCart(@PathVariable("id") @NotEmpty @Positive Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/product";
    }

    @PostMapping(value = "item/{id}/quantity/{quantity}")
    public String setQuantity(Model model,
                              @PathVariable("id") @NotEmpty @Positive Long productId,
                              @PathVariable("quantity") @NotEmpty @Positive Integer quantity) {
        cartService.setQuantity(productId, quantity);
        return "redirect:/product";
    }
}
