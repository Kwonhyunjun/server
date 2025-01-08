package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;

import java.util.Objects;

public enum ReservationEntityStatus {

  PENDING, PAID, REFUNDED, CANCELLED, REJECTED;

  public ReservationStatus toDomain() {
    for(ReservationStatus status : ReservationStatus.values()) {
      if(Objects.equals(status.name(), this.name())) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid status: " + this.name());
  }

  public static ReservationEntityStatus fromDomain(ReservationStatus reservationStatus) { return ReservationEntityStatus.valueOf(reservationStatus.name());}
}
