package com.carrental.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RentalPeriodTest {
    @Test
    void shouldRejectNullDates() {
        // Arrange
        LocalDate validDate = LocalDate.of(2026, 10, 10);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new RentalPeriod(null, validDate)
        );

        assertThrows(
                NullPointerException.class,
                () -> new RentalPeriod(validDate, null)
        );
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        // Arrange
        LocalDate startDate = LocalDate.of(2026, 10, 10);
        LocalDate endDate = LocalDate.of(2026, 10, 9);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new RentalPeriod(startDate, endDate)
        );
    }

    @Test
    void shouldAllowSingleDayRentalPeriod() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 10, 10);

        // Act & Assert
        assertDoesNotThrow(() -> new RentalPeriod(date, date));
    }

    @Test
    void shouldOverlapWhenPeriodsShareDays() {
        // Arrange
        RentalPeriod firstPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13));

        RentalPeriod secondPeriod = new RentalPeriod(LocalDate.of(2026, 10, 13),
                LocalDate.of(2026, 10, 16));

        // Act & Assert
        assertTrue(firstPeriod.overlaps(secondPeriod));
        assertTrue(secondPeriod.overlaps(firstPeriod));
    }

    @Test
    void shouldOverlapWhenPeriodsAreTheSame() {
        // Arrange
        LocalDate startDate = LocalDate.of(2026, 10, 10);
        LocalDate endDate = LocalDate.of(2026, 10, 13);
        RentalPeriod firstPeriod = new RentalPeriod(startDate, endDate);
        RentalPeriod secondPeriod = new RentalPeriod(startDate, endDate);

        // Act & Assert
        assertTrue(firstPeriod.overlaps(secondPeriod));
        assertTrue(secondPeriod.overlaps(firstPeriod));
    }

    @Test
    void shouldOverlapWhenOnePeriodIsInsideAnother() {
        // Arrange
        RentalPeriod firstPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 16));

        RentalPeriod secondPeriod = new RentalPeriod(LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14));

        // Act & Assert
        assertTrue(firstPeriod.overlaps(secondPeriod));
        assertTrue(secondPeriod.overlaps(firstPeriod));
    }

    @Test
    void shouldNotOverlapWhenPeriodsAreAdjacent() {
        // Arrange
        RentalPeriod firstPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13));

        RentalPeriod secondPeriod = new RentalPeriod(LocalDate.of(2026, 10, 14),
                LocalDate.of(2026, 10, 16));

        // Act & Assert
        assertFalse(firstPeriod.overlaps(secondPeriod));
        assertFalse(secondPeriod.overlaps(firstPeriod));
    }

    @Test
    void shouldNotOverlapWhenPeriodsAreSeparated() {
        // Arrange
        RentalPeriod firstPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13));

        RentalPeriod secondPeriod = new RentalPeriod(LocalDate.of(2026, 10, 17),
                LocalDate.of(2026, 10, 20));

        // Act & Assert
        assertFalse(firstPeriod.overlaps(secondPeriod));
        assertFalse(secondPeriod.overlaps(firstPeriod));
    }

    @Test
    void shouldCalculateSingleDayDuration() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 10, 10);
        RentalPeriod period = new RentalPeriod(date, date);

        // Act & Assert
        assertEquals(1, period.durationInDays());
    }

    @Test
    void shouldCalculateMultipleDaysDuration() {
        // Arrange
        LocalDate startDate = LocalDate.of(2026, 10, 10);
        LocalDate endDate = LocalDate.of(2026, 10, 13);
        RentalPeriod period = new RentalPeriod(startDate, endDate);

        // Act & Assert
        assertEquals(4, period.durationInDays());
    }
}
