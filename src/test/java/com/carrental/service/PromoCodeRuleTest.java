package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.DiscountType;
import com.carrental.domain.PromoCode;
import com.carrental.domain.RentalPeriod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromoCodeRuleTest {
    private final PromoCodeRule rule = new PromoCodeRule();

    private PricingContext context(PromoCode promoCode) {
        return new PricingContext(
                CarCategory.ECONOMY,
                new RentalPeriod(
                        LocalDate.of(2026, 10, 12),
                        LocalDate.of(2026, 10, 16)
                ),
                new BigDecimal("120.00"),
                new BigDecimal("600.00"),
                promoCode
        );
    }

    @Test
    void shouldApplyPercentageDiscount() {
        PromoCode promo = new PromoCode(
                "PROMO20",
                DiscountType.PERCENTAGE,
                new BigDecimal("20")
        );

        BigDecimal actual = rule.apply(
                new BigDecimal("600.00"),
                context(promo)
        );

        assertEquals(0, new BigDecimal("480.00").compareTo(actual));
    }

    @Test
    void shouldApplyFixedAmountDiscount() {
        PromoCode promo = new PromoCode(
                "MINUS50",
                DiscountType.FIXED_AMOUNT,
                new BigDecimal("50.00")
        );

        BigDecimal actual = rule.apply(
                new BigDecimal("600.00"),
                context(promo)
        );

        assertEquals(0, new BigDecimal("550.00").compareTo(actual));
    }

    @Test
    void shouldKeepPriceUnchangedWithoutPromoCode() {
        BigDecimal actual = rule.apply(
                new BigDecimal("600.00"),
                context(null)
        );

        assertEquals(0, new BigDecimal("600.00").compareTo(actual));
    }
}
