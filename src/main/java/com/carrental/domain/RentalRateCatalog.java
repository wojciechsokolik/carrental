package com.carrental.domain;

import java.math.BigDecimal;
import java.util.Map;

public class RentalRateCatalog {
    private final Map<CarCategory, BigDecimal> dailyRates = Map.of(
            CarCategory.ECONOMY, new BigDecimal("120.00"),
            CarCategory.STANDARD, new BigDecimal("180.00"),
            CarCategory.PREMIUM, new BigDecimal("350.00"),
            CarCategory.VAN, new BigDecimal("260.00")
    );

    public BigDecimal getDailyRate(CarCategory carCategory) {
        BigDecimal rate = dailyRates.get(carCategory);

        if (rate == null) {
            throw new IllegalArgumentException("No daily rate for category: " + carCategory);
        }

        return rate;
    }
}
