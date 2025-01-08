package com.bkmarriott.reservationservice.reservation.application.exception;

public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
