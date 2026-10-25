package com.carrental.service;

import com.carrental.domain.Car;
import com.carrental.domain.RentalPeriod;
import com.carrental.domain.Reservation;
import com.carrental.domain.ReservationStatus;

import java.util.List;

public class AvailabilityService {
    public boolean isAvailable(Car car, RentalPeriod requestedPeriod, List<Reservation> reservations) {
        return reservations.stream()
                .noneMatch(reservation ->
                        reservation.getCar().equals(car)
                                && blocksCar(reservation)
                                && overlaps(requestedPeriod, reservation)
                );
    }

    private boolean blocksCar(Reservation reservation) {
        return reservation.getStatus() != ReservationStatus.CANCELLED
                && reservation.getStatus() != ReservationStatus.COMPLETED;
    }

    private boolean overlaps(RentalPeriod requestedPeriod, Reservation reservation) {
        return requestedPeriod.overlaps(reservation.getRentalPeriod());
    }
}
