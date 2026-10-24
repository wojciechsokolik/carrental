package com.carrental.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record RentalPeriod(LocalDate startDate, LocalDate endDate) {
    public RentalPeriod {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date must be after start date."
            );
        }
    }

    public boolean overlaps(RentalPeriod other) {
        Objects.requireNonNull(other, "Other rental period cannot be null.");

        return !startDate.isAfter(other.endDate())
                && !other.startDate().isAfter(endDate);
    }

    public long durationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
