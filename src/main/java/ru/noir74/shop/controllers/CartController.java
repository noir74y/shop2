package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.services.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public String get(Model model) {
        return "cart";
    }

    @PostMapping(value = "item/{itemId}/quantity/{quantity}")
    public String setQuantity(Model model,
                                 @PathVariable("itemId") Long itemId,
                                 @PathVariable("quantity") Integer quantity) {
        return "cart";
    }

    @PostMapping(value = "item/{itemId}/remove")
    public String removeItem(Model model, @PathVariable("itemId") Long itemId) {
        return "cart";
    }

    @PostMapping(value = "order")
    public String makeOrder(Model model) {
        return "orders";
    }

}
