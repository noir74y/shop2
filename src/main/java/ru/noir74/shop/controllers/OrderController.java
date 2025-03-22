package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.services.OrderService;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public String getALl(Model model) {
        var orders = orderMapper.bulkDomain2Dto(orderService.getALl());
        return "orders";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable("id") Long id) {
        var order = orderMapper.domain2dto(orderService.get(id));
        return "order";
    }
}
