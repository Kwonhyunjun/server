package com.bkmarriott.reservationservice.reservation.domain.vo;

public record Payment(
        Long paymentId,
        Long reservationId,
        Long originalPrice,
        Long finalPrice,
        String paymentType,
        String transactionalId,
        Long appliedCoupon
) {}
