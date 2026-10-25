package com.carrental.service;

import com.carrental.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AvailabilityServiceTest {
    private final AvailabilityService availabilityService = new AvailabilityService();

    private Reservation createReservation(Car car, RentalPeriod period) {
        Client client = new Client("Jan", "Kowalski");

        return new Reservation(
                client,
                car,
                period,
                BigDecimal.valueOf(120)
        );
    }

    private Reservation createReservationWithStatus(Car car, RentalPeriod period, ReservationStatus status) {
        Reservation reservation = createReservation(car, period);

        switch (status) {
            case CREATED -> {
            }
            case CONFIRMED -> reservation.confirm();
            case IN_PROGRESS -> {
                reservation.confirm();
                reservation.start();
            }
            case COMPLETED -> {
                reservation.confirm();
                reservation.start();
                reservation.complete();
            }
            case CANCELLED -> reservation.cancel();
        }

        return reservation;
    }

    @Test
    void shouldBeAvailableWhenThereAreNoReservations() {
        // Arrange
        Car car = new Car("WOT 12345", CarCategory.ECONOMY, new Branch("WAW", "Warsaw"));
        RentalPeriod rentalPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14));

        // Act
        boolean isAvailable = availabilityService.isAvailable(car, rentalPeriod, List.of());

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    void shouldBeAvailableWhenReservationIsForAnotherCar() {
        // Arrange
        Branch branch = new Branch("WAW", "Warsaw");
        Car car1 = new Car("WOT 12345", CarCategory.ECONOMY, branch);
        Car car2 = new Car("WOT 56789", CarCategory.ECONOMY, branch);
        RentalPeriod rentalPeriod = new RentalPeriod(LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14));
        Reservation reservation = createReservation(car1, rentalPeriod);
        List<Reservation> reservations = List.of(reservation);

        // Act
        boolean isAvailable = availabilityService.isAvailable(car2, rentalPeriod, reservations);

        // Assert
        assertTrue(isAvailable);
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            names = {"CANCELLED", "COMPLETED"}
    )
    void shouldBeAvailableWhenOverlappingReservationDoesNotBlockCar(ReservationStatus status) {
        // Arrange
        Car car = new Car("WOT 12345", CarCategory.ECONOMY, new Branch("WAW", "Warsaw"));

        RentalPeriod bookedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14)
        );

        RentalPeriod requestedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14)
        );

        Reservation reservation = createReservationWithStatus(car, bookedPeriod, status);

        // Act
        boolean isAvailable = availabilityService.isAvailable(car, requestedPeriod, List.of(reservation));

        // Assert
        assertTrue(isAvailable);
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            names = {"CREATED", "CONFIRMED", "IN_PROGRESS"}
    )
    void shouldNotBeAvailableWhenOverlappingReservationBlocksCar(ReservationStatus status) {
        // Arrange
        Car car = new Car(
                "WOT 12345",
                CarCategory.ECONOMY,
                new Branch("WAW", "Warsaw")
        );

        RentalPeriod bookedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14)
        );

        RentalPeriod requestedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 16)
        );

        Reservation reservation = createReservationWithStatus(car, bookedPeriod, status);

        // Act
        boolean isAvailable = availabilityService.isAvailable(car, requestedPeriod, List.of(reservation));

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    void shouldBeAvailableWhenExistingReservationDoesNotOverlap() {
        // Arrange
        Car car = new Car("WOT 12345", CarCategory.ECONOMY, new Branch("WAW", "Warsaw"));

        RentalPeriod bookedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14)
        );

        RentalPeriod requestedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20)
        );

        Reservation reservation = createReservation(car, bookedPeriod);

        // Act
        boolean isAvailable = availabilityService.isAvailable(car, requestedPeriod, List.of(reservation));

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    void shouldNotBeAvailableWhenOneOfMultipleReservationsConflicts() {
        // Arrange
        Branch branch = new Branch("WAW", "Warsaw");

        Car requestedCar = new Car("WOT 12345", CarCategory.ECONOMY, branch);
        Car anotherCar = new Car("WOT 45678", CarCategory.ECONOMY, branch);

        RentalPeriod requestedPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 16)
        );

        RentalPeriod nonOverlappingPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 5)
        );

        Reservation reservationForAnotherCar =
                createReservation(anotherCar, requestedPeriod);

        Reservation nonOverlappingReservation =
                createReservation(requestedCar, nonOverlappingPeriod);

        Reservation conflictingReservation =
                createReservationWithStatus(
                        requestedCar,
                        requestedPeriod,
                        ReservationStatus.CONFIRMED
                );

        List<Reservation> reservations = List.of(
                reservationForAnotherCar,
                nonOverlappingReservation,
                conflictingReservation
        );

        // Act
        boolean isAvailable = availabilityService.isAvailable(
                requestedCar, requestedPeriod, reservations
        );

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    void shouldNotBeAvailableWhenDifferentCarInstancesHaveSameRegistrationNumber() {
        // Arrange
        Branch branch = new Branch("WAW", "Warsaw");

        Car bookedCar = new Car("WOT 12345", CarCategory.ECONOMY, branch);
        Car requestedCar = new Car("WOT 12345", CarCategory.ECONOMY, branch);

        RentalPeriod rentalPeriod = new RentalPeriod(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 14)
        );

        Reservation reservation = createReservation(bookedCar, rentalPeriod);

        // Act
        boolean isAvailable = availabilityService.isAvailable(
                requestedCar,
                rentalPeriod,
                List.of(reservation)
        );

        // Assert
        assertFalse(isAvailable);
    }
}
