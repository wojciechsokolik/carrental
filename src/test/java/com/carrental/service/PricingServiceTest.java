package com.carrental.service;

import com.carrental.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PricingServiceTest {
    private final RentalRateCatalog rentalRateCatalog = new RentalRateCatalog();
    private final PricingService pricingService = new PricingService(
            rentalRateCatalog,
            List.of(new WeekendSurchargeRule(),
                    new LongTermDiscountRule())
    );

    @ParameterizedTest
    @CsvSource({
            "ECONOMY, 600.00",
            "STANDARD, 900.00",
            "PREMIUM, 1750.00",
            "VAN, 1300.00"
    })
    void shouldCalculateBasePriceForEachCarCategory(CarCategory carCategory, String expectedPrice) {
        // Arrange
        RentalPeriod rentalPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14)
        );

        // Act
        BigDecimal actualPrice = pricingService.calculateBasePrice(carCategory, rentalPeriod);

        // Assert
        assertEquals(new BigDecimal(expectedPrice), actualPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "2026-10-10, 2026-10-10, 120.00",
            "2026-10-10, 2026-10-13, 480.00",
            "2026-10-30, 2026-11-02, 480.00"
    })
    void shouldCalculateBasePriceForDifferentRentalPeriods(LocalDate startDate, LocalDate endDate, String expectedPrice) {
        // Arrange
        RentalPeriod rentalPeriod = new RentalPeriod(startDate, endDate);

        // Act
        BigDecimal actualPrice = pricingService.calculateBasePrice(CarCategory.ECONOMY, rentalPeriod);

        // Assert
        assertEquals(new BigDecimal(expectedPrice), actualPrice);
    }

    @Test
    void shouldApplyWeekendSurchargeBeforeLongTermDiscount() {
        // Arrange
        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 18)
        );

        // Act
        BigDecimal actualPrice = pricingService.calculateFinalPrice(
                CarCategory.ECONOMY,
                period
        );

        // Assert
        assertEquals(new BigDecimal("788.40"), actualPrice);
    }

    @Test
    void shouldEnforceMinimumPriceAfterApplyingAllPricingRules() {
        // Arrange
        PricingRule largeDiscountRule = (currentPrice, context) ->
                currentPrice.multiply(new BigDecimal("0.10"));

        PricingService service = new PricingService(
                new RentalRateCatalog(),
                List.of(largeDiscountRule)
        );

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 16)
        );

        // Act
        BigDecimal actualPrice = service.calculateFinalPrice(
                CarCategory.ECONOMY,
                period
        );

        // Assert
        assertEquals(new BigDecimal("420.00"), actualPrice);
    }
}
