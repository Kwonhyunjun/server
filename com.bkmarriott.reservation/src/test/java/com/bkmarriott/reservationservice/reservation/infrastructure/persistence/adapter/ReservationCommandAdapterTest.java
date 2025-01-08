package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[Infrastructure] ReservationCommandAdapter Test")
@RepositoryTest
public class ReservationCommandAdapterTest {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ReservationCommandAdapter reservationCommandAdapter;

    PaymentForCreate paymentForCreate;
    ReservationForCreate reservationForCreate;

    @BeforeEach
    void setUp(){
        paymentForCreate = new PaymentForCreate(null, "credit_card", "4111111111111111", "12/25", "123", 1L, 190000L, 171000L);
        reservationForCreate = new ReservationForCreate(1L, 1L, RoomType.DELUXE, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-01"), paymentForCreate);
    }

    @Test
    @DisplayName("[성공] 예약 저장 테스트 - 주어진 정보로 예약을 생성한다.")
    void createReservation_successTest() {
        // Given
        PaymentForCreate paymentForCreate = new PaymentForCreate(null, "credit_card", "4111111111111111", "12/25", "123", 1L, 190000L, 171000L);
        ReservationForCreate reservationForCreate = new ReservationForCreate(1L, 1L, RoomType.DELUXE, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"), paymentForCreate);

        // When
        Reservation reservation = reservationCommandAdapter.createReservation(reservationForCreate);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(reservation.getReservationId()),
                () -> Assertions.assertEquals(reservation.getUserId(), reservationForCreate.userId()),
                () -> Assertions.assertEquals(reservation.getHotelId(), reservationForCreate.hotelId()),
                () -> Assertions.assertEquals(reservation.getUserId(), reservationForCreate.userId()),
                () -> Assertions.assertEquals(reservation.getStartDate(), reservationForCreate.startDate()),
                () -> Assertions.assertEquals(reservation.getEndDate(), reservationForCreate.endDate()),
                () -> Assertions.assertEquals(reservation.getRoomType(), reservationForCreate.roomType()),
                () -> Assertions.assertEquals(reservation.getStatus(), ReservationStatus.PENDING),
                () -> Assertions.assertNull(reservation.getRoomId())
        );
    }

    @Test
    @DisplayName("[성공] 예약 상태 업데이트 테스트 - 예약 Id와 상태 정보로 예약상태를 수정한다.")
    void updateReservationStatus_successTest() {
        // Given
        Long reservationId = reservationRepository.save(ReservationEntity.from(reservationForCreate)).getId();
        ReservationStatus changeType = ReservationStatus.PAID;

        // When
        Reservation updatedReservation = reservationCommandAdapter.updateReservationStatus(reservationId, changeType);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(updatedReservation.getStatus(), changeType)
        );
    }

    @Test
    @DisplayName("[실패] 예약 상태 업데이트 테스트 - 예약 없을 시 ResourceNotFoundException 예외를 반환한다.")
    void updateReservationStatus_notFoundTest() {
        // Given
        Long reservationId = Long.MAX_VALUE;

        // When & Then
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> reservationCommandAdapter.updateReservationStatus(reservationId, ReservationStatus.PAID))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("해당하는 예약 정보가 없습니다.")
        );
    }
}
