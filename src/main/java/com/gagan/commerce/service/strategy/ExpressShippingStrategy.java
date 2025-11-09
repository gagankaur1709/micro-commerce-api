package com.gagan.commerce.service.strategy;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;

import java.math.BigDecimal;

public abstract class ExpressShippingStrategy implements ShippingStrategy {
    public void calculateShippingCost(Order order) {
        BigDecimal minCost = new BigDecimal("15.0");
        BigDecimal percentRate = new BigDecimal("0.10");

        BigDecimal calculatedCost = order.getBase_price().multiply(percentRate);
        BigDecimal shippingCost = calculatedCost.max(minCost);

        order.setBase_price(order.getBase_price().add(shippingCost));
    }

    public ShippingType getShippingStrategy() {
        return ShippingType.EXPRESS;
    }
}
