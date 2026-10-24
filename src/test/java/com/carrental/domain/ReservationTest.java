package com.carrental.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationTest {

    private Reservation createReservation() {
        Branch branch = new Branch("WAW", "Warsaw");
        Car car = new Car("WA12345", CarCategory.ECONOMY, branch);
        Client client = new Client("Jan", "Kowalski");

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 13)
        );

        return new Reservation(
                client,
                car,
                period,
                new BigDecimal("360.00")
        );
    }

    private Reservation createReservationWithStatus(ReservationStatus status) {
        Reservation reservation = createReservation();

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
    void shouldHaveCreatedStatusWhenReservationIsCreated() {
        // Arrange
        Reservation reservation = createReservation();

        // Assert
        assertEquals(ReservationStatus.CREATED, reservation.getStatus());
    }

    @Test
    void shouldConfirmCreatedReservation() {
        // Arrange
        Reservation reservation = createReservation();

        // Act
        reservation.confirm();

        // Assert
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldStartConfirmedReservation() {
        // Arrange
        Reservation reservation = createReservationWithStatus(ReservationStatus.CONFIRMED);

        // Act
        reservation.start();

        // Assert
        assertEquals(ReservationStatus.IN_PROGRESS, reservation.getStatus());
    }

    @Test
    void shouldCompleteStartedReservation() {
        // Arrange
        Reservation reservation = createReservationWithStatus(ReservationStatus.IN_PROGRESS);

        // Act
        reservation.complete();

        // Assert
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
    }


    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            names = {"CREATED", "CONFIRMED"}
    )
    void shouldCancelReservation(ReservationStatus initialStatus) {
        // Arrange
        Reservation reservation = createReservationWithStatus(initialStatus);

        // Act
        reservation.cancel();

        // Assert
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = {"CREATED", "CONFIRMED"}
    )
    void shouldRejectCancellation(ReservationStatus initialStatus) {
        // Arrange
        Reservation reservation = createReservationWithStatus(initialStatus);

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::cancel);

        assertEquals(initialStatus, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = {"CREATED"}
    )
    void shouldRejectConfirmation(ReservationStatus initialStatus) {
        // Arrange
        Reservation reservation = createReservationWithStatus(initialStatus);

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::confirm);

        assertEquals(initialStatus, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = {"CONFIRMED"}
    )
    void shouldRejectStarting(ReservationStatus initialStatus) {
        // Arrange
        Reservation reservation = createReservationWithStatus(initialStatus);

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::start);

        assertEquals(initialStatus, reservation.getStatus());
    }

    @ParameterizedTest
    @EnumSource(
            value = ReservationStatus.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = {"IN_PROGRESS"}
    )
    void shouldRejectCompleting(ReservationStatus initialStatus) {
        // Arrange
        Reservation reservation = createReservationWithStatus(initialStatus);

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::complete);

        assertEquals(initialStatus, reservation.getStatus());
    }
}
