package com.gagan.commerce.service.tax;
import com.gagan.commerce.domain.Order;
import java.math.BigDecimal;

public interface TaxService {
    BigDecimal calculateTax(Order order);
}
