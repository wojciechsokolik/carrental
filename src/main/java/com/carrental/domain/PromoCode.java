package com.carrental.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PromoCode(String code, DiscountType type, BigDecimal value) {
    public PromoCode {
        Objects.requireNonNull(code);
        Objects.requireNonNull(type);
        Objects.requireNonNull(value);

        if (code.isBlank()) {
            throw new IllegalArgumentException("Promo code cannot be blank.");
        }

        if (value.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Discount value must be positive."
            );
        }

        if (type == DiscountType.PERCENTAGE
                && value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    "Percentage discount cannot exceed 100%"
            );
        }
    }
}
