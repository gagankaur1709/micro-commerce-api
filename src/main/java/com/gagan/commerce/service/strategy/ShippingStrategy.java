package com.gagan.commerce.service.strategy;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;

import java.math.BigDecimal;

public interface ShippingStrategy { // Strategy pattern
    public void calculateShippingCost (Order order);

    public ShippingType getShippingStrategy();
}
