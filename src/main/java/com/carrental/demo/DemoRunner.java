package com.carrental.demo;

import com.carrental.domain.*;
import com.carrental.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DemoRunner {

    public void run() {
        PricingService pricingService = new PricingService(
                new RentalRateCatalog(),
                List.of(
                        new WeekendSurchargeRule(),
                        new LongTermDiscountRule()
                )
        );

        NotificationService notificationService =
                new EmailNotificationService();

        ReservationService reservationService = new ReservationService(
                new AvailabilityService(),
                pricingService,
                notificationService
        );

        Branch branch = new Branch("WAW", "Warsaw");
        Client client = new Client("Jan", "Kowalski", "jankowalski@gmail.com");

        Car car = new Car(
                "WA12345",
                CarCategory.ECONOMY,
                branch
        );

        reservationService.addCar(car);

        RentalPeriod period = new RentalPeriod(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 16)
        );

        PromoCode promoCode = new PromoCode(
                "PROMO20",
                DiscountType.PERCENTAGE,
                new BigDecimal("20")
        );

        Reservation reservation = reservationService.createReservation(
                client,
                CarCategory.ECONOMY,
                branch,
                period,
                promoCode
        );
    }
}