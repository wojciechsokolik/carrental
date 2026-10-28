package com.carrental.service;

import java.math.BigDecimal;

public class MinimumPriceRule implements PricingRule {
    private static final BigDecimal MINIMUM_PRICE_MULTIPLIER = new BigDecimal("0.70");

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        BigDecimal minimumPrice = context.basePrice()
                .multiply(MINIMUM_PRICE_MULTIPLIER);

        return currentPrice.max(minimumPrice);
    }
}
