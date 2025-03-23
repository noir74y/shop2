package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ImageService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public String get(Model model) {
        var cart = cartService.get();
        return "cart";
    }

    @PostMapping(value = "item/{itemId}/quantity/{quantity}")
    public String setQuantity(Model model,
                              @PathVariable("itemId") Long itemId,
                              @PathVariable("quantity") Integer quantity) {
        cartService.setQuantity(itemId, quantity);
        return "cart";
    }

    @PostMapping(value = "item/{itemId}/remove")
    public String removeItem(Model model, @PathVariable("itemId") Long itemId) {
        cartService.removeFromCart(itemId);
        return "cart";
    }

    @PostMapping(value = "order")
    public String makeOrder(Model model) {
        cartService.makeOrder();
        return "orders";
    }

}
