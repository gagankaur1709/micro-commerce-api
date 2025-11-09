package com.gagan.commerce.controller;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;
import com.gagan.commerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService; //constructor injection

    @Autowired // can be skipped as this has just one constructor
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderForId(id);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order, @RequestParam ShippingType shippingType) {
        Order createdOrder = orderService.createOrder(order, shippingType);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public Order editOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        return orderService.editOrderForId(id, orderDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrd1er(@PathVariable Long id) {
        orderService.deleteOrderForId(id);
    }

    @GetMapping
    public List<Order> getAllOrder() {
        return orderService.getAllOrder();
    }
}
