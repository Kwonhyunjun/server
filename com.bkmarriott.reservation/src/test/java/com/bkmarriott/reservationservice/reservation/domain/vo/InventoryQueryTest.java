package com.bkmarriott.reservationservice.reservation.domain.vo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("[Domain] [Unit] InventoryQueryTest VO Test")
public class InventoryQueryTest {

    @Test
    @DisplayName("[성공] 객체 생성 테스트 - 유효한 정보가 주어진 경우 객체를 생성한다.")
    void inventoryConstruct_successTest() {
        // Given
        Long hotelId = 1L;
        Long userId = 0L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();;
        RoomType roomType = RoomType.DELUXE;
        PaymentForCreate paymentForCreate = null;

        ReservationForCreate reservationForCreate = new ReservationForCreate(hotelId, userId, roomType, startDate, endDate, paymentForCreate);

        // When & Then
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> new InventoryQuery(hotelId, startDate, endDate, roomType)),
                () -> Assertions.assertDoesNotThrow(() -> InventoryQuery.fromReservationForCreate(reservationForCreate))
        );
    }
}
