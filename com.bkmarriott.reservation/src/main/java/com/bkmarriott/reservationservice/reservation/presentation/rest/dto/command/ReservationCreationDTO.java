package com.bkmarriott.reservationservice.reservation.presentation.rest.dto.command;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;

import java.time.LocalDate;

public class ReservationCreationDTO {
    public record Request(
            Long hotelId,
            RoomType roomType,
            LocalDate startDate,
            LocalDate endDate,
            PaymentInfo paymentInfo
    ){
        public ReservationForCreate toDomain(Long userId) {
            return new ReservationForCreate(
                    hotelId,
                    userId,
                    roomType,
                    startDate,
                    endDate,
                    new PaymentForCreate(
                            null,
                            paymentInfo().method,
                            paymentInfo().cardNumber(),
                            paymentInfo().expiryDate(),
                            paymentInfo().cvv(),
                            paymentInfo().appliedCoupon(),
                            paymentInfo().originalPrice(),
                            paymentInfo().finalPrice()
                    )
            );
        }
    }

    public record PaymentInfo(
            String method,
            String cardNumber,
            String expiryDate,
            String cvv,
            Long appliedCoupon,
            Long originalPrice,
            Long finalPrice
    ){}

    public record Response(
            Long reservationId
    ){
        public static Response from(Reservation reservation){
            return new Response(
                    reservation.getReservationId()
            );
        }
    }
}
