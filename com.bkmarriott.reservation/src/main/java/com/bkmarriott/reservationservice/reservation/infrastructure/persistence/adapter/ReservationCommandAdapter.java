package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.ReservationEntityStatus;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationCommandAdapter implements ReservationCommandOutputPort {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public Reservation createReservation(ReservationForCreate reservationForCreate) {
        ReservationEntity reservation = ReservationEntity.from(reservationForCreate);
        return reservationRepository.save(reservation).toDomain();
    }

    @Override
    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationStatus changeStatus) {
        ReservationEntity reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("해당하는 예약 정보가 없습니다."));
        reservation.updateStatus(ReservationEntityStatus.fromDomain(changeStatus));
        return reservation.toDomain();
    }
}
