package com.carrental.service;

import com.carrental.domain.CarCategory;
import com.carrental.domain.RentalPeriod;
import com.carrental.domain.RentalRateCatalog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class PricingService {
    private final RentalRateCatalog rentalRateCatalog;

    private final List<PricingRule> pricingRules;

    private final MinimumPriceRule minimumPriceRule = new MinimumPriceRule();

    public PricingService(RentalRateCatalog rentalRateCatalog, List<PricingRule> pricingRules) {
        this.rentalRateCatalog = Objects.requireNonNull(rentalRateCatalog);
        this.pricingRules = List.copyOf(pricingRules);
    }

    public BigDecimal calculateBasePrice(CarCategory category, RentalPeriod period) {
        return rentalRateCatalog.getDailyRate(category)
                .multiply(BigDecimal.valueOf(period.durationInDays()));
    }

    public BigDecimal calculateFinalPrice(CarCategory category, RentalPeriod period) {
        BigDecimal basePrice = calculateBasePrice(category, period);
        PricingContext context = new PricingContext(category, period, rentalRateCatalog.getDailyRate(category), basePrice);

        BigDecimal currentPrice = basePrice;

        for (PricingRule rule : pricingRules) {
            currentPrice = rule.apply(currentPrice, context);
        }

        currentPrice = minimumPriceRule.apply(currentPrice, context);

        return currentPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
