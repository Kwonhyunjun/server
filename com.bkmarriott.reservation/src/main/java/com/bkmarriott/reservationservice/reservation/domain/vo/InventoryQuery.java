package com.bkmarriott.reservationservice.reservation.domain.vo;

import java.time.LocalDate;

public record InventoryQuery(
        Long hotelId,
        LocalDate startDate,
        LocalDate endDate,
        RoomType roomType
) {
    public static InventoryQuery fromReservationForCreate(ReservationForCreate reservation) {
        return new InventoryQuery(reservation.hotelId(), reservation.startDate(), reservation.endDate(), reservation.roomType());
    }
}
