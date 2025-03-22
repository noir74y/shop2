package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.models.dto.ItemDtoReq;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ItemService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping
    public String getPage(Model model,
                          @RequestParam(defaultValue = "1", required = false, name = "page") String page,
                          @RequestParam(defaultValue = "10", required = false, name = "size") String size,
                          @RequestParam(defaultValue = "title", required = false, name = "title") String sort)  {
        return "items";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") Integer id) {
        return "item";
    }

    @PostMapping
    public String create(@ModelAttribute ItemDtoReq itemDtoReq) {
        return "redirect:/item";
    }

    @PostMapping(value = "{id}")
    public String update(@ModelAttribute ItemDtoReq itemDtoReq, @PathVariable("id") Integer id) {
        return "redirect:/item";
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public String delete(@PathVariable("id") Integer id) {
        return "redirect:/item";
    }

    @PostMapping(value = "{id}/addToCart")
    public String addToCart(@PathVariable("id") Long id) {
        return "redirect:/cart";
    }

    @PostMapping(value = "{id}/removeFromCart")
    public String removeFromCart(@PathVariable("id") Long id) {
        return "redirect:/cart";
    }

}
