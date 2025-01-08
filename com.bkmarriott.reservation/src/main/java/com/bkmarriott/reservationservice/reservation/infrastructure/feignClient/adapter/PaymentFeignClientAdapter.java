package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.feign.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.Payment;
import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.PaymentClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.PaymentRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFeignClientAdapter implements PaymentOutputPort {

    private final PaymentClient paymentClient;

    @Override
    public Payment processPayment(PaymentForCreate paymentForCreate, Long reservationId) {
        log.info("[PaymentFeignClientAdapter] [processPayment] payment ::: {} ", paymentForCreate);
        return paymentClient.processPayment(PaymentRequestDto.from(paymentForCreate, reservationId)).toDomain();
    }

    @Retry(name = "processRefundRetry", fallbackMethod = "fallbackProcessRefund")
    @CircuitBreaker(name = "payment", fallbackMethod = "fallbackProcessRefund")
    @Override
    public Payment processRefund(Long paymentId) {
        log.info("[PaymentFeignClientAdapter] [processRefund] paymentId ::: {} ", paymentId);
        return paymentClient.processRefund(paymentId).toDomain();
    }


    public Payment fallbackProcessRefund(Long paymentId, Throwable throwable) {
        log.error("[PaymentFeignClientAdapter] [fallbackProcessRefund] Payment ::: {}, ErrorMessage ::: {} , ... ", paymentId, throwable.getMessage());
        // TODO 만약 환불이 안될 경우 히스토리를 남기든 추가 작업 필요
        return null;
    }

}
