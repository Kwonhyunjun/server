package com.bkmarriott.reservationservice.reservation.domain.vo;

import java.time.LocalDate;

public record ReservationForCreate (
        Long hotelId,
        RoomType roomType,
        LocalDate startDate,
        LocalDate endDate,
        Payment payment
) {}
