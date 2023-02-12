package com.project.controllers;

import com.project.domain.relations.ArrangementInOrder;
import com.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/user/{username}")
    public List<ArrangementInOrder> findAllOrders(@PathVariable String username) {
        return this.orderService.findAllByOrderAndFutureArrangement(username);
    }

    @GetMapping("/totalPrice/{username}")
    public Double getTotalPrice(@PathVariable String username) {
        return this.orderService.getTotalPrice(username);
    }


}
