package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentDto;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payment-service")
public interface PaymentClient {

    @PostMapping("/api/vi/payments")
    PaymentDto processPayment(@RequestBody PaymentRequestDto dto);

    @PostMapping("/api/vi/payments/refund/{paymentId}")
    PaymentDto processRefund(@PathVariable Long paymentId);

}
