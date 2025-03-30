package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.services.CartService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;
    private final ItemMapper itemMapper;

    @GetMapping
    public String get(Model model) {
        var items = itemMapper.bulkDomain2dto(cartService.findAll());
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    @PostMapping(value = "product/{productId}/add")
    public String addToCart(@PathVariable("productId") @NotEmpty @Positive Long productId) {
        cartService.addToCart(productId);
        return "redirect:/cart";
    }

    @PostMapping(value = "item/{productId}/remove")
    public String removeFromCart(@PathVariable("productId") @NotEmpty @Positive Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping(value = "item/{productId}/quantity/{quantity}")
    public String setQuantity(Model model,
                              @PathVariable("productId") @NotEmpty @Positive Long productId,
                              @PathVariable("quantity") @NotEmpty @Positive Integer quantity) {
        cartService.setQuantity(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping(value = "order")
    public String makeOrder() {
        cartService.makeOrder();
        return "redirect:/order";
    }

}
