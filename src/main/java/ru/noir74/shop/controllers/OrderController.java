package ru.noir74.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.noir74.shop.models.mappers.OrderMapper;
import ru.noir74.shop.services.OrderService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    @Transactional(readOnly = true)
    public String getALl(Model model) {
        var orders = orderMapper.bulkDomain2Dto(orderService.getAll());
        return "order-list";
    }

    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public String get(Model model, @PathVariable("id") @NotEmpty @Positive Long id) {
        var order = orderMapper.domain2dto(orderService.get(id));
        return "order";
    }
}
