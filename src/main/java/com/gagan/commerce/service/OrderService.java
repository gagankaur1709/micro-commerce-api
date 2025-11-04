package com.gagan.commerce.service;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.exception.OrderNotFound;
import com.gagan.commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order createOrder(Order order) {
        order.setId(null);
        return orderRepository.save(order);
    }

    public Order getOrderForId(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public Order editOrderForId(Long id, Order orderDetails) {
        Order order = getOrderForId(id);
        order.setCustomer_name(orderDetails.getCustomer_name());
        order.setPrice(orderDetails.getPrice());
        order.setTotal(orderDetails.getTotal());
        return orderRepository.save(order);
    }

    public void deleteOrderForId(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFound("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }
}
