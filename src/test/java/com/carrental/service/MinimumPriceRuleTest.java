package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.RentalPeriod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinimumPriceRuleTest {

    private final MinimumPriceRule rule = new MinimumPriceRule();

    @ParameterizedTest
    @CsvSource({
            "1000.00, 600.00, 700.00",
            "1000.00, 850.00, 850.00",
            "1000.00, 700.00, 700.00"
    })
    void shouldEnforceMinimumPrice(
            String basePrice,
            String currentPrice,
            String expectedPrice
    ) {
        // Arrange
        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 16)
        );

        BigDecimal dailyRate = new BigDecimal("200.00");

        PricingContext context = new PricingContext(
                CarCategory.ECONOMY,
                period,
                dailyRate,
                new BigDecimal(basePrice)
        );

        // Act
        BigDecimal actualPrice = rule.apply(
                new BigDecimal(currentPrice),
                context
        );

        // Assert
        assertEquals(
                0,
                new BigDecimal(expectedPrice).compareTo(actualPrice)
        );
    }
}