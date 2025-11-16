package com.gagan.commerce.service;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;
import com.gagan.commerce.exception.OrderNotFoundException;
import com.gagan.commerce.repository.OrderRepository;
import com.gagan.commerce.service.strategy.ShippingStrategy;
import com.gagan.commerce.service.strategy.ShippingStrategyFactory;
import com.gagan.commerce.service.tax.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShippingStrategyFactory strategyFactory;
    private final TaxService taxService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ShippingStrategyFactory strategyFactory, TaxService taxService) {
        this.orderRepository = orderRepository;
        this.strategyFactory = strategyFactory;
        this.taxService = taxService;
    }

    public Order createOrder(Order order, ShippingType shippingType) {
        ShippingStrategy strategy = strategyFactory.getStrategy(shippingType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid shipping type: " + shippingType);
        }
        BigDecimal tax = taxService.calculateTax(order);
        strategy.calculateShippingCost(order);
        order.setTotal(order.getBase_price().add(tax));
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
        order.setTotal(orderDetails.getTotal());
        if (!orderDetails.getZipCode().equalsIgnoreCase(order.getZipCode())) {
            order.setZipCode(orderDetails.getZipCode());
            BigDecimal tax = taxService.calculateTax(order);
            order.setTotal(orderDetails.getTotal().add(tax));
        }

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
