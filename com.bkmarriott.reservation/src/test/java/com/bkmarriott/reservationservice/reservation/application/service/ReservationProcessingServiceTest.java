package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.exception.PaymentException;
import com.bkmarriott.reservationservice.reservation.application.exception.ReservationProcessingException;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.*;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] ReservationProcessingServiceTest Unit test")
public class ReservationProcessingServiceTest {

    @InjectMocks private ReservationProcessingService reservationProcessingService;
    @Mock private InventoryService inventoryService;
    @Mock private ReservationCommandOutputPort reservationCommandOutputPort;
    @Mock private CouponOutputPort couponOutputPort;
    @Mock private PaymentOutputPort paymentOutputPort;

    PaymentForCreate paymentForCreate;
    ReservationForCreate reservationForCreate;

    @BeforeEach
    void setUp(){
        paymentForCreate = new PaymentForCreate(null, "credit_card", "4111111111111111", "12/25", "123", 1L, 190000L, 171000L);
        reservationForCreate = new ReservationForCreate(1L, 1L, RoomType.DELUXE, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-01"), paymentForCreate);
    }

    @Test
    @DisplayName("[성공] 예약 준비 테스트 - 적용된 쿠폰의 유효성을 검사하고 예약을 PENDING 상태로 저장한다.")
    void prepareReservation_successTest(){
        // Given
        CouponDto mockCoupon = new CouponDto(1L, reservationForCreate.userId(), null, null, null);
        Reservation mockReservation = Reservation.builder()
                .reservationId(1L)
                .userId(1L)
                .hotelId(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .roomType(RoomType.DELUXE)
                .roomId(null)
                .status(ReservationStatus.PENDING)
                .build();

        Mockito.when(couponOutputPort.verifyCoupon(reservationForCreate.paymentForCreate().appliedCoupon())).thenReturn(mockCoupon);
        Mockito.when(reservationCommandOutputPort.createReservation(reservationForCreate)).thenReturn(mockReservation);

        // When
        Long reservationId = reservationProcessingService.prepareReservation(reservationForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(reservationId, mockReservation.getReservationId()),
                () -> Assertions.assertEquals(mockReservation.getStatus(), ReservationStatus.PENDING)
        );
        Mockito.verify(couponOutputPort).verifyCoupon(reservationForCreate.paymentForCreate().appliedCoupon());
        Mockito.verify(reservationCommandOutputPort).createReservation(reservationForCreate);
    }

    @Test
    @DisplayName("[실패] 예약 준비 테스트 - 쿠폰 유효성 검사에서 FeignClient 오류 발생시 ReservationProcessingException 예외를 발생한다.")
    void prepareReservation_FailureTest(){
        // Given
        Mockito.when(couponOutputPort.verifyCoupon(reservationForCreate.paymentForCreate().appliedCoupon())).thenThrow(FeignException.class);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> reservationProcessingService.prepareReservation(reservationForCreate))
                        .isInstanceOf(ReservationProcessingException.class)
                        .hasMessage("예약 준비 중 오류가 발생했습니다.")
        );
    }

    @Test
    @DisplayName("[실패] 예약준비 테스트 - 예약 저장 과정에서 RuntimeException 오류 발생시 ReservationProcessingException 예외를 발생한다.")
    void prepareReservation_FailureTest2(){
        // Given
        CouponDto mockCoupon = new CouponDto(1L, reservationForCreate.userId(), null, null, null);

        Mockito.when(couponOutputPort.verifyCoupon(reservationForCreate.paymentForCreate().appliedCoupon())).thenReturn(mockCoupon);
        Mockito.when(reservationCommandOutputPort.createReservation(reservationForCreate)).thenThrow(RuntimeException.class);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> reservationProcessingService.prepareReservation(reservationForCreate))
                        .isInstanceOf(ReservationProcessingException.class)
                        .hasMessage("예약 준비 중 오류가 발생했습니다.")
        );
    }

    @Test
    @DisplayName("[성공] 결제 테스트 - 결제 서비스에 요청하고 저장된 Payment 를 반환한다.")
    void processPayment_successTest(){
        // Given
        Long reservationId = 1L;
        Payment mockPayment = new Payment(1L, 1L,190000L, 171000L,"paymentType", "transactionalId", 1L);

        Mockito.when(paymentOutputPort.processPayment(paymentForCreate, reservationId)).thenReturn(mockPayment);

        // When
        Payment result = reservationProcessingService.processPayment(reservationId, paymentForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.paymentId(), 1L),
                () -> Assertions.assertEquals(result.finalPrice(), paymentForCreate.finalPrice())
        );
    }

    @Test
    @DisplayName("[실패] 결제 테스트 - 결제 처리 중 FeignClient 오류 발생시 ReservationProcessingException 예외를 발생한다.")
    void processPayment_FailureTest(){
        // Given
        Long reservationId = 1L;

        Mockito.when(reservationProcessingService.processPayment(reservationId, paymentForCreate)).thenThrow(FeignException.class);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> reservationProcessingService.processPayment(reservationId, paymentForCreate))
                        .isInstanceOf(PaymentException.class)
                        .hasMessage("결제가 진행되지 않았습니다.")
        );
    }

    @Test
    @DisplayName("[성공] 예약확정 테스트 - 결제가 완료되면 쿠폰 사용 처리와 예약 객실을 증가시킨다.")
    void confirmReservation_successTest(){
        // Given
        Long reservationId = 1L;
        CouponDto mockCoupon = new CouponDto(1L, reservationForCreate.userId(), null, null, null);
        Payment mockPayment = new Payment(1L, 1L,190000L, 171000L,"paymentType", "transactionalId", 1L);
        List<Inventory> inventoryList = List.of(
                Inventory.of(reservationForCreate.hotelId(), LocalDate.parse("2025-02-01"), reservationForCreate.roomType(), 80, 79),
                Inventory.of(reservationForCreate.hotelId(), LocalDate.parse("2025-02-02"), reservationForCreate.roomType(), 80, 52)
        );

        Mockito.when(couponOutputPort.useCoupon(paymentForCreate.appliedCoupon())).thenReturn(mockCoupon);
        Mockito.when(inventoryService.updateTotalReserved(reservationId)).thenReturn(inventoryList);

        // When
        reservationProcessingService.confirmReservation(reservationId, mockPayment);

        // Then
        Mockito.verify(couponOutputPort).useCoupon(paymentForCreate.appliedCoupon()); // 쿠폰 사용이 호출됐는지 검증
        Mockito.verify(inventoryService).updateTotalReserved(reservationId); // 예약 객실 수가 증가했는지 검증
    }

    @Test
    @DisplayName("[성공] 예약확정 테스트 - 결제가 완료되면 쿠폰 사용 처리와 예약 객실을 증가시킨다.")
    void confirmReservation_FailureTest(){
        // Given
        Long reservationId = 1L;
        Payment mockPayment = new Payment(1L, 1L,190000L, 171000L,"paymentType", "transactionalId", 1L);

        Mockito.when(couponOutputPort.useCoupon(paymentForCreate.appliedCoupon())).thenThrow(FeignException.class);

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> reservationProcessingService.confirmReservation(reservationId, mockPayment))
                        .isInstanceOf(ReservationProcessingException.class)
                        .hasMessage("예약 확정 중 오류가 발생했습니다.")
        );

        Mockito.verify(paymentOutputPort).processRefund(mockPayment.paymentId());
        Mockito.verify(reservationCommandOutputPort).updateReservationStatus(reservationId, ReservationStatus.REFUNDED);
    }


}
