package com.carrental.service;


import com.carrental.domain.Reservation;

public class EmailNotificationService implements NotificationService {
    @Override
    public void sendReservationConfirmation(Reservation reservation) {
        System.out.printf("""

                ========== EMAIL ==========
                To: %s
                Subject: Car rental reservation

                Reservation ID: %s
                Car: %s
                Rental period: %s - %s
                Status: %s
                Total price: %s PLN
                ===========================
                %n""",
                reservation.getClient().getEmail(),
                reservation.getId(),
                reservation.getCar().getRegistrationNumber(),
                reservation.getRentalPeriod().startDate(),
                reservation.getRentalPeriod().endDate(),
                reservation.getStatus(),
                reservation.getPrice()
        );
    }
}
