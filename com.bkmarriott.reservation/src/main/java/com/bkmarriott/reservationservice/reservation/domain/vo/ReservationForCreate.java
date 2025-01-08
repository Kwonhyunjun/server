package com.bkmarriott.reservationservice.reservation.domain.vo;

import java.time.LocalDate;

public record ReservationForCreate (
        Long hotelId,
        Long userId,
        RoomType roomType,
        LocalDate startDate,
        LocalDate endDate,
        PaymentForCreate paymentForCreate
) {}
