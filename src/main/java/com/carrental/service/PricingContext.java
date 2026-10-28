package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.RentalPeriod;

import java.math.BigDecimal;
import java.util.Objects;

public record PricingContext(CarCategory category, RentalPeriod period, BigDecimal dailyRate, BigDecimal basePrice) {
    public PricingContext {
        Objects.requireNonNull(category);
        Objects.requireNonNull(period);
        Objects.requireNonNull(dailyRate);
        Objects.requireNonNull(basePrice);
    }
}
