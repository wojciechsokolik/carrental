package com.carrental.service;

import com.carrental.domain.Reservation;

public interface NotificationService {
    void sendReservationConfirmation(Reservation reservation);
}
