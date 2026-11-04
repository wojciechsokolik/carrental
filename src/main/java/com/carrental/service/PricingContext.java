package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.PromoCode;
import com.carrental.domain.RentalPeriod;

import java.math.BigDecimal;
import java.util.Objects;

public record PricingContext(CarCategory category, RentalPeriod period, BigDecimal dailyRate, BigDecimal basePrice,
                             PromoCode promoCode) {
    public PricingContext {
        Objects.requireNonNull(category);
        Objects.requireNonNull(period);
        Objects.requireNonNull(dailyRate);
        Objects.requireNonNull(basePrice);
    }

    public PricingContext(
            CarCategory category,
            RentalPeriod period,
            BigDecimal dailyRate,
            BigDecimal basePrice) {
        this(category, period, dailyRate, basePrice, null);
    }
}
