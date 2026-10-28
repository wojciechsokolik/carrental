package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.RentalPeriod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeekendSurchargeRuleTest {
    private final WeekendSurchargeRule rule =
            new WeekendSurchargeRule();

    @ParameterizedTest
    @CsvSource({
            "2026-10-12, 2026-10-14, 360.00",
            "2026-10-09, 2026-10-10, 258.00",
            "2026-10-10, 2026-10-11, 276.00",
            "2026-10-30, 2026-11-02, 516.00"
    })
    void shouldApplyWeekendSurcharge(
            LocalDate startDate,
            LocalDate endDate,
            String expectedPrice
    ) {
        // Arrange
        RentalPeriod period = new RentalPeriod(startDate, endDate);

        BigDecimal dailyRate = new BigDecimal("120.00");

        BigDecimal basePrice = dailyRate.multiply(
                BigDecimal.valueOf(period.durationInDays())
        );

        PricingContext context = new PricingContext(
                CarCategory.ECONOMY,
                period,
                dailyRate,
                basePrice
        );

        // Act
        BigDecimal actualPrice = rule.apply(basePrice, context);

        // Assert
        assertEquals(0, new BigDecimal(expectedPrice).compareTo(actualPrice));
    }
}
