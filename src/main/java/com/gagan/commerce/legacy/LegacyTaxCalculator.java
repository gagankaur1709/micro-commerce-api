package com.gagan.commerce.legacy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LegacyTaxCalculator {

    public BigDecimal calculate(BigDecimal basePrice, String zipCode) {

        // Fake logic: 8% tax for "90210", 5% for everyone else
        BigDecimal rate;
        if ("90210".equals(zipCode)) {
            rate = new BigDecimal("0.08");
        } else {
            rate = new BigDecimal("0.05");
        }

        return basePrice.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
