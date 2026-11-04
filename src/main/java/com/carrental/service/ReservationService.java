package com.carrental.service;

import com.carrental.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationService {
    private final AvailabilityService availabilityService;
    private final PricingService pricingService;

    private final List<Car> cars = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private final NotificationService notificationService;

    public ReservationService(
            AvailabilityService availabilityService,
            PricingService pricingService,
            NotificationService notificationService
    ) {
        this.availabilityService = Objects.requireNonNull(availabilityService);
        this.pricingService = Objects.requireNonNull(pricingService);
        this.notificationService = Objects.requireNonNull(notificationService);
    }

    public void addCar(Car car) {
        cars.add(Objects.requireNonNull(car));
    }

    public Reservation createReservation(
            Client client,
            CarCategory category,
            Branch branch,
            RentalPeriod period,
            PromoCode promoCode
    ) {
        Objects.requireNonNull(client);
        Objects.requireNonNull(category);
        Objects.requireNonNull(branch);
        Objects.requireNonNull(period);

        Car availableCar = cars.stream()
                .filter(car -> car.getCarCategory() == category)
                .filter(car -> car.getBranch().equals(branch))
                .filter(car -> availabilityService.isAvailable(
                        car, period, reservations
                ))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No available car for the selected category and period."
                ));

        BigDecimal price = pricingService.calculateFinalPrice(category, period, promoCode);

        Reservation reservation = new Reservation(client, availableCar, period, price);

        reservations.add(reservation);

        notificationService.sendReservationConfirmation(reservation);

        return reservation;
    }

    public List<Reservation> getReservations() {
        return List.copyOf(reservations);
    }
}
