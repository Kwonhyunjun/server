package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] InventoryService Unit test")
public class InventoryServiceTest {
    @InjectMocks private InventoryService inventoryService;
    @Mock private InventoryCommandOutputPort inventoryCommandOutputPort;
    @Mock private ReservationQueryOutputPort reservationQueryOutputPort;
    @Mock private InventoryQueryOutputPort inventoryQueryOutputPort;

    @Test
    @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 성공 테스트] 예약 기간에 따라 가용 가능한 최소 객실 수를 반환한다.")
    void getAvailableRoomCount_successTest(){
        // Given
        InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);
        int idx = 0;
        int totalInventory = 80;
        int[] totalReserved = new int[]{77, 40, 59};

        int minAvailableCnt = totalInventory;

        ArrayList<Inventory> inventories = new ArrayList<>();
        List<LocalDate> localDates = query.startDate().datesUntil(query.endDate()).toList();
        for(LocalDate date : localDates){
            minAvailableCnt = Math.min(minAvailableCnt, totalInventory-totalReserved[idx]);
            inventories.add(Inventory.of(query.hotelId(), date, query.roomType(), 80, totalReserved[idx++]));
        }
        int expectedRoomCount = minAvailableCnt;

        Mockito.when(inventoryQueryOutputPort.findInventoryFromReservation(query)).thenReturn(inventories);

        // When
        int availableRoomCount = inventoryService.getAvailableRoomCount(query);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedRoomCount, availableRoomCount)
        );
    }

    @Test
    @DisplayName("[예약 기간에 따른 Inventory 가용 객실 수 반환 실패 테스트] 예약정보에 해당하는 객실 정보가 없다면 예외 발생")
    void getAvailableRoomCount_failTest(){
        // Given
        InventoryQuery query = new InventoryQuery(101L, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-03"), RoomType.DELUXE);

        ArrayList<Inventory> inventories = new ArrayList<>();
        Mockito.when(inventoryQueryOutputPort.findInventoryFromReservation(query)).thenReturn(inventories);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> inventoryService.getAvailableRoomCount(query))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("해당 예약정보에 해당하는 객실 정보를 찾을 수 없습니다.")
        );
    }

}
