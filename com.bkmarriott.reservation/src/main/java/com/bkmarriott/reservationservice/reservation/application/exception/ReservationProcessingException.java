package com.bkmarriott.reservationservice.reservation.application.exception;

public class ReservationProcessingException extends RuntimeException {

    public ReservationProcessingException(String message) {
        super(message);
    }
}
