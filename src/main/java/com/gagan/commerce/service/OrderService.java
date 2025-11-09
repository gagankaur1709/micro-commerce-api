package com.gagan.commerce.service;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;
import com.gagan.commerce.exception.OrderNotFoundException;
import com.gagan.commerce.repository.OrderRepository;
import com.gagan.commerce.service.strategy.ShippingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Map<ShippingType, ShippingStrategy> shippingStrategies; // Inject dependency in form of map for O(1) lookup

    @Autowired
    public OrderService(OrderRepository orderRepository, List<ShippingStrategy> shippingStrategies) {
        this.orderRepository = orderRepository;
        this.shippingStrategies = shippingStrategies.stream().collect(Collectors.toMap(ShippingStrategy::getShippingStrategy, Function.identity()));
    }

    public Order createOrder(Order order, ShippingType shippingType) {
        ShippingStrategy strategy = shippingStrategies.get(shippingType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid shipping type: " + shippingType);
        }
        strategy.calculateShippingCost(order);
        order.setId(null);
        return orderRepository.save(order);
    }

    public Order getOrderForId(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public Order editOrderForId(Long id, Order orderDetails) {
        Order order = getOrderForId(id);
        order.setCustomer_name(orderDetails.getCustomer_name());
        order.setBase_price(orderDetails.getBase_price());
        return orderRepository.save(order);
    }

    public void deleteOrderForId(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }
}
