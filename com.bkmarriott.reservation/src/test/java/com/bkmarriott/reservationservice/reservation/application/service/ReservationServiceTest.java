package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] ReservationServiceTest Unit test")
public class ReservationServiceTest {

    @InjectMocks private ReservationService reservationService;
    @Mock private InventoryService inventoryService;
    @Mock private ReservationProcessingService reservationProcessingService;
    @Mock private ReservationCommandOutputPort reservationCommandOutputPort;

    @Test
    @DisplayName("[성공] 예약 생성 테스트 - 예약 및 결제 프로세스를 진행하고 저장된 예약정보를 반환한다.")
    void createReservation_successTest(){
        // Given
        PaymentForCreate paymentForCreate = new PaymentForCreate(null, "credit_card", "4111111111111111", "12/25", "123", 1L, 190000L, 171000L);
        ReservationForCreate reservationForCreate = new ReservationForCreate(1L, 1L, RoomType.DELUXE, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"), paymentForCreate);
        InventoryQuery inventoryQuery = InventoryQuery.fromReservationForCreate(reservationForCreate);
        Long mockReservationId = 1L;
        Payment mockPayment = new Payment(1L, 1L,190000L, 171000L,"paymentType", "transactionalId", 1L);
        Reservation mockReservation = Reservation.builder()
                .reservationId(mockReservationId)
                .userId(1L)
                .hotelId(1L)
                .startDate(LocalDate.parse("2025-02-01"))
                .endDate(LocalDate.parse("2025-02-02"))
                .roomType(RoomType.DELUXE)
                .roomId(null)
                .status(ReservationStatus.PAID)
                .build();

        Mockito.when(inventoryService.getAvailableRoomCount(inventoryQuery)).thenReturn(2);
        Mockito.when(reservationProcessingService.prepareReservation(reservationForCreate)).thenReturn(1L);
        Mockito.when(reservationProcessingService.processPayment(mockReservationId, reservationForCreate.paymentForCreate())).thenReturn(mockPayment);
        Mockito.when(reservationCommandOutputPort.updateReservationStatus(mockReservationId, ReservationStatus.PAID)).thenReturn(mockReservation);
        Mockito.doNothing().when(reservationProcessingService).confirmReservation(mockReservationId, mockPayment);

        // When
        Reservation result = reservationService.createReservation(reservationForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(mockReservation, result)
        );
    }
}
