package com.carrental.service;

import com.carrental.domain.RentalPeriod;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekendSurchargeRule implements PricingRule {
    private static final BigDecimal WEEKEND_SURCHARGE_RATE =
            new BigDecimal("0.15");

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        int weekendDays = calculateWeekendDays(context.period());

        BigDecimal weekendSurcharge = context.dailyRate()
                .multiply(BigDecimal.valueOf(weekendDays))
                .multiply(WEEKEND_SURCHARGE_RATE);

        return currentPrice.add(weekendSurcharge);
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private int calculateWeekendDays(RentalPeriod rentalPeriod) {
       LocalDate date = rentalPeriod.startDate();
       int weekendDays = 0;

       while (!date.isAfter(rentalPeriod.endDate())) {
           if (isWeekend(date)) {
               weekendDays++;
           }
           date = date.plusDays(1);
       }

       return weekendDays;
    }
}
