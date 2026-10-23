package com.carrental.domain;

import java.time.LocalDate;
import java.util.Objects;

public record RentalPeriod(LocalDate startDate, LocalDate endDate) {
    public RentalPeriod {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "End date must be after start date."
            );
        }
    }
}
