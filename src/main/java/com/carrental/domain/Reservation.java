package com.carrental.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Reservation {
    private final UUID id;
    private final Client client;
    private final Car car;
    private final RentalPeriod rentalPeriod;
    private final BigDecimal price;
    private ReservationStatus status;

    public Reservation(Client client, Car car, RentalPeriod rentalPeriod, BigDecimal price) {
        this.id = UUID.randomUUID();
        this.client = Objects.requireNonNull(client, "Client cannot be null");
        this.car = Objects.requireNonNull(car, "Car cannot be null");
        this.rentalPeriod = Objects.requireNonNull(rentalPeriod, "Rental period cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.status = ReservationStatus.CREATED;
    }

    public void confirm() {
        if (status != ReservationStatus.CREATED) {
            throw new IllegalStateException("Cannot confirm reservation in status: " + status);
        }
        status = ReservationStatus.CONFIRMED;
    }

    public void start() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot start reservation in status: " + status);
        }
        status = ReservationStatus.IN_PROGRESS;
    }

    public void complete() {
        if (status != ReservationStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot complete reservation in status: " + status);
        }
        status = ReservationStatus.COMPLETED;
    }
}
