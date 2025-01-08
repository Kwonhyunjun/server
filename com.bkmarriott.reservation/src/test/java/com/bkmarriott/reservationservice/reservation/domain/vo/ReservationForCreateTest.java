package com.bkmarriott.reservationservice.reservation.domain.vo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("[Domain] [Unit] ReservationForCreate VO Test")
public class ReservationForCreateTest {

    @Test
    @DisplayName("[성공] 객체 생성 테스트 - 유효한 정보가 주어진 경우 객체를 생성한다.")
    void reservationForCreateConstruct_successTest() {
        // Given
        Long hotelId = 1L;
        Long userId = 1L;
        RoomType roomType = RoomType.DELUXE;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();;
        PaymentForCreate paymentForCreate = null;

        // When & Then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> new ReservationForCreate(hotelId, userId, roomType, startDate, endDate, paymentForCreate))
        );
    }
}
