package com.bkmarriott.reservationservice.reservation.application.outputport;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;

public interface ReservationCommandOutputPort {

    Reservation createReservation(ReservationForCreate reservationForCreate);

    Reservation updateReservationStatus(Long reservationId, ReservationStatus changeStatus);
}
