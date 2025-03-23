package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.noir74.shop.models.dto.ItemDtoReq;
import ru.noir74.shop.models.mappers.ItemMapper;
import ru.noir74.shop.services.CartService;
import ru.noir74.shop.services.ItemService;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;
    private final ItemMapper itemMapper;

    @GetMapping
    public String getPage(Model model,
                          @RequestParam(defaultValue = "1", required = false, name = "page") String page,
                          @RequestParam(defaultValue = "10", required = false, name = "size") String size,
                          @RequestParam(defaultValue = "TITLE", required = false, name = "sort") String sort) {
        var items = itemMapper.bulkDomain2DtoResp(itemService.getPage(page, size, sort));
        return "items";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") Long id) {
        var item = itemMapper.domain2dtoResp(itemService.get(id));
        return "item";
    }

    @PostMapping
    public String create(@ModelAttribute ItemDtoReq itemDtoReq) {
        itemService.create(itemMapper.dtoReq2domain(itemDtoReq));
        return "redirect:/item";
    }

    @PostMapping(value = "{id}")
    public String update(@ModelAttribute ItemDtoReq itemDtoReq, @PathVariable("id") Long id) {
        itemService.update(itemMapper.dtoReq2domain(itemDtoReq));
        return "redirect:/item";
    }

    @PostMapping(value = "{id}", params = "_method=delete")
    public String delete(@PathVariable("id") Long id) {
        itemService.delete(id);
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
