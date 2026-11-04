package com.carrental.service;

import com.carrental.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationServiceTest {
    private ReservationService reservationService;

    private Branch branch;
    private Client client;
    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        PricingService pricingService = new PricingService(
                new RentalRateCatalog(),
                List.of(
                        new WeekendSurchargeRule(),
                        new LongTermDiscountRule()
                )
        );

        reservationService = new ReservationService(
                new AvailabilityService(),
                pricingService,
                new EmailNotificationService()
        );

        branch = new Branch("WAW", "Warsaw");
        client = new Client("Jan", "Kowalski", "jankowalski@gmail.com");

        car1 = new Car("WA12345", CarCategory.ECONOMY, branch);
        car2 = new Car("WA67890", CarCategory.ECONOMY, branch);
    }

    @Test
    void shouldCreateReservationForAvailableCar() {
        // Arrange
        reservationService.addCar(car1);

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14)
        );

        // Act
        Reservation reservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        // Assert
        assertEquals(car1, reservation.getCar());
        assertEquals(ReservationStatus.CREATED, reservation.getStatus());
        assertEquals(new BigDecimal("360.00"), reservation.getPrice());
        assertEquals(1, reservationService.getReservations().size());
    }

    @Test
    void shouldThrowExceptionWhenNoCarIsAvailable() {
        // Arrange
        reservationService.addCar(car1);

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14)
        );

        reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> reservationService.createReservation(
                        client,
                        CarCategory.ECONOMY,
                        branch,
                        period,
                        null
                )
        );

        assertEquals(1, reservationService.getReservations().size());
    }

    @Test
    void shouldSelectAnotherCarWhenFirstIsReserved() {
        // Arrange
        reservationService.addCar(car1);
        reservationService.addCar(car2);

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14)
        );

        // Act
        Reservation firstReservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        Reservation secondReservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        // Assert
        assertEquals(car1, firstReservation.getCar());
        assertEquals(car2, secondReservation.getCar());
        assertEquals(2, reservationService.getReservations().size());
    }

    @Test
    void shouldAllowReservationAfterCancellation() {
        // Arrange
        reservationService.addCar(car1);

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14)
        );

        Reservation firstReservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        firstReservation.cancel();

        // Act
        Reservation secondReservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                null
        );

        // Assert
        assertEquals(car1, secondReservation.getCar());
        assertEquals(ReservationStatus.CREATED, secondReservation.getStatus());
        assertEquals(2, reservationService.getReservations().size());
    }
}
