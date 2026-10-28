package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.RentalPeriod;

import java.math.BigDecimal;

public class LongTermDiscountRule implements PricingRule {
    private static final int MIN_DAYS = 7;
    private static final BigDecimal MULTIPLIER = new BigDecimal("0.90");

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        if (context.period().durationInDays() >= MIN_DAYS) {
            return currentPrice.multiply(MULTIPLIER);
        }

        return currentPrice;
    }
}
