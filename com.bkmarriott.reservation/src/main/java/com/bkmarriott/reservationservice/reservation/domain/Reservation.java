package com.bkmarriott.reservationservice.reservation.domain;

import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Reservation {

  private Long reservationId;
  private Long userId;
  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;
  private RoomType roomType;
  private ReservationStatus status;
  private Long roomId;
}
