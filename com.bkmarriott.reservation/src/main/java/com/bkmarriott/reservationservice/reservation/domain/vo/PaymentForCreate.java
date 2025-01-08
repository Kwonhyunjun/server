package com.bkmarriott.reservationservice.reservation.domain.vo;

public record PaymentForCreate(
        Long paymentId,
        String method,
        String cardNumber,
        String expiryDate,
        String cvv,
        Long appliedCoupon,
        Long originalPrice,
        Long finalPrice
) {}
