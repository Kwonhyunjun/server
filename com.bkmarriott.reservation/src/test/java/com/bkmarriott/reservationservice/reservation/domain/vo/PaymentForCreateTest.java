package com.bkmarriott.reservationservice.reservation.domain.vo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("[Domain] [Unit] PaymentTest VO Test")
public class PaymentForCreateTest {

    @Test
    @DisplayName("[성공] 객체 생성 테스트 - 유효한 정보가 주어진 경우 객체를 생성한다.")
    void paymentConstruct_successTest() {
        // Given
        String method = "credit_card";
        String cardNumber = "4111111111111111";
        String expiryDate = "12/25";
        String cvv = "123";
        Long appliedCoupon = 1L;
        Long originalPrice = 190000L;
        Long finalPrice = 171000L;

        // When & Then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> new PaymentForCreate(null, method, cardNumber, expiryDate, cvv, appliedCoupon, originalPrice, finalPrice))
        );
    }
}
