package com.carrental.service;

import com.carrental.domain.PromoCode;

import java.math.BigDecimal;

public class PromoCodeRule implements PricingRule {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        PromoCode promo = context.promoCode();

        if (promo == null) {
            return currentPrice;
        }

        return switch (promo.type()) {
            case PERCENTAGE -> {
                BigDecimal multiplier = BigDecimal.ONE.subtract(
                        promo.value().divide(ONE_HUNDRED)
                );
                yield currentPrice.multiply(multiplier);
            }

            case FIXED_AMOUNT ->
                    currentPrice.subtract(promo.value());
        };
    }
}
