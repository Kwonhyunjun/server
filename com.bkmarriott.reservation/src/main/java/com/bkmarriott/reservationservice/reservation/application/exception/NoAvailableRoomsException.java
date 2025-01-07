package com.bkmarriott.reservationservice.reservation.application.exception;

public class NoAvailableRoomsException extends RuntimeException {

    public NoAvailableRoomsException(String message) {
        super(message);
    }
}