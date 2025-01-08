package com.bkmarriott.reservationservice.reservation.presentation.rest.controller;

import com.bkmarriott.reservationservice.reservation.application.service.ReservationService;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.command.ReservationCreationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@WebMvcTest(ReservationCommandController.class)
@DisplayName("[Presentation] ReservationCommandController Unit Test")
public class ReservationCommandControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("[성공] 예약 & 결제 생성 테스트 - 예약 및 결제 성공시 예약 ID를 반환")
    void timeAttackCouponIssuance_successTest_issuanceSuccess() throws Exception {
        // Given
        String requestUrl = "/api/v1/reservations";
        ReservationCreationDTO.PaymentInfo paymentInfo = new ReservationCreationDTO.PaymentInfo("credit_card", "4111111111111111", "12/25", "123", 1L, 190000L, 171000L);
        String requestBody = objectMapper.writeValueAsString(
                new ReservationCreationDTO.Request(1L, RoomType.DELUXE, LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-02"), paymentInfo)
        );
        Reservation mockReservation = Reservation.builder()
                .reservationId(1L)
                .userId(1L)
                .hotelId(101L)
                .startDate(LocalDate.parse("2025-02-01"))
                .endDate(LocalDate.parse("2025-02-02"))
                .roomType(RoomType.DELUXE)
                .roomId(null)
                .status(ReservationStatus.PAID)
                .build();

        Mockito.when(reservationService.createReservation(ArgumentMatchers.any(
                ReservationForCreate.class
        ))).thenReturn(mockReservation);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 1L)
                        .header("X-Role", "CUSTOMER"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(ReservationCreationDTO.Response.from(mockReservation))));
    }
}
