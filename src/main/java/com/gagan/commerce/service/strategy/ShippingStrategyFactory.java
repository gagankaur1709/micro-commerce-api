package com.gagan.commerce.service.strategy;

import com.gagan.commerce.domain.ShippingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ShippingStrategyFactory {

    // This map is now owned by the factory
    private final Map<ShippingType, ShippingStrategy> shippingStrategies;

    // 2. The constructor injects all strategies and builds the map
    @Autowired
    public ShippingStrategyFactory(List<ShippingStrategy> shippingStrategies) {
        this.shippingStrategies = shippingStrategies.stream().collect(Collectors.toMap(ShippingStrategy::getShippingStrategy, Function.identity()));
    }


    // This is the "factory method"
    public ShippingStrategy getStrategy(ShippingType shippingType) {
        ShippingStrategy strategy = shippingStrategies.get(shippingType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid shipping type: " + shippingType);
        }
        return strategy;
    }
}
