package com.gagan.commerce.service.strategy;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;

import java.math.BigDecimal;

public abstract class StandardShippingStrategy implements ShippingStrategy {

    public void calculateShippingCost(Order order) {
        order.setBase_price(order.getBase_price().add(new BigDecimal("10")));
    }

    public ShippingType getShippingStrategy() {
        return ShippingType.STANDARD;
    }
}
