package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.Payment;

public record PaymentDto(
        Long paymentId,
        Long reservationId,
        Long originalPrice,
        Long finalPrice,
        String paymentType,
        String transactionalId,
        Long appliedCoupon
) {
    public Payment toDomain(){
        return new Payment(
                paymentId,
                reservationId,
                originalPrice,
                finalPrice,
                paymentType,
                transactionalId,
                appliedCoupon
        );
    }
}
