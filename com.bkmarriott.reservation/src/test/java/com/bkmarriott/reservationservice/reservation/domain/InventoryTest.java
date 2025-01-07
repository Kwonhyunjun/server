package com.bkmarriott.reservationservice.reservation.domain;

import com.bkmarriott.reservationservice.reservation.application.exception.NoAvailableRoomsException;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[Domain] InventoryTest Unit test")
public class InventoryTest {

    @Test
    @DisplayName("[실패] 객체 생성 테스트 - 유효한 정보가 주어진 경우 객체를 생성한다.")
    void getAvailableRoomCount_failureTest() {

        Inventory countSameinventory =Inventory.of(1L, LocalDate.now(), RoomType.DELUXE, 80, 80);
        Inventory countOverInventory =Inventory.of(1L, LocalDate.now(), RoomType.DELUXE, 80, 81);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(countSameinventory::getAvailableRoomCount)
                        .isInstanceOf(NoAvailableRoomsException.class)
                        .hasMessage("남은 방이 없습니다."),
                () -> assertThatThrownBy(countOverInventory::getAvailableRoomCount)
                        .isInstanceOf(NoAvailableRoomsException.class)
                        .hasMessage("남은 방이 없습니다.")
        );
    }

}
