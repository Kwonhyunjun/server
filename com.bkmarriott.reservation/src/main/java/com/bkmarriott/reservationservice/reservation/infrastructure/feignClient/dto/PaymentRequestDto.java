package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto;

import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;

public record PaymentRequestDto(
        Long reservationId,
        String method,
        String cardNumber,
        String expiryDate,
        String cvv,
        Long appliedCoupon,
        Long originalPrice,
        Long finalPrice
) {

    public static PaymentRequestDto from(PaymentForCreate paymentForCreate, Long reservationId){
        return new PaymentRequestDto(
                reservationId,
                paymentForCreate.method(),
                paymentForCreate.cardNumber(),
                paymentForCreate.expiryDate(),
                paymentForCreate.cvv(),
                paymentForCreate.appliedCoupon(),
                paymentForCreate.originalPrice(),
                paymentForCreate.finalPrice()
        );
    }
}
