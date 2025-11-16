package com.gagan.commerce.service.tax;

import com.gagan.commerce.domain.Order;
import com.gagan.commerce.legacy.LegacyTaxCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxServiceAdapter implements TaxService{

    private final LegacyTaxCalculator legacyCalculator;

    public TaxServiceAdapter() {
        this.legacyCalculator = new LegacyTaxCalculator();
    }

    @Override
    public BigDecimal calculateTax(Order order) {
        return legacyCalculator.calculate(order.getBase_price(), order.getZipCode());
    }
}
