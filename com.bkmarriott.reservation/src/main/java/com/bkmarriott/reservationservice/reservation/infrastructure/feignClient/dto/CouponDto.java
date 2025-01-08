package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto;

import java.time.LocalDateTime;

public record CouponDto(
        Long id,
        Long userId,
        LocalDateTime issuanceAt,
        LocalDateTime spendingAt,
        LocalDateTime expiredAt
) {}
