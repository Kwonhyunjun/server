package com.bkmarriott.reservationservice.reservation.domain.vo;

public record Payment(
        String method,
        String cardNumber,
        String expiryDate,
        String cvv,
        Long appliedCoupon,
        Long originalPrice,
        Long finalPrice
) {}
