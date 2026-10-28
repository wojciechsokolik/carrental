package com.carrental.service;

import java.math.BigDecimal;

public interface PricingRule {

    BigDecimal apply(
            BigDecimal currentPrice,
            PricingContext context
    );
}
