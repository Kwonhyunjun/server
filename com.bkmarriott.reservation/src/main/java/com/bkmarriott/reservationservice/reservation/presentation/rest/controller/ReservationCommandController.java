package com.bkmarriott.reservationservice.reservation.presentation.rest.controller;

import com.bkmarriott.reservationservice.reservation.application.service.ReservationService;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.auth.Actor;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.command.ReservationCreationDTO;
import com.bkmarriott.reservationservice.reservation.presentation.rest.util.auth.LoginActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationCommandController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationCreationDTO.Response> createReservation(
            @RequestBody ReservationCreationDTO.Request request,
            @LoginActor Actor actor
    ){

        log.info("[ReservationCommandController] [createReservation] hotel_id :: {}, card_number :: {}", request.hotelId(), request.paymentInfo().cardNumber());

        Reservation reservation = reservationService.createReservation(request.toDomain(actor.userId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationCreationDTO.Response.from(reservation));
    }
}
