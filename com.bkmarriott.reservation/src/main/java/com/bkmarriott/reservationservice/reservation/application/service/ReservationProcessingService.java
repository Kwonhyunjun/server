package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.exception.PaymentException;
import com.bkmarriott.reservationservice.reservation.application.exception.ReservationProcessingException;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.Payment;
import com.bkmarriott.reservationservice.reservation.domain.vo.PaymentForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationProcessingService {

    private final InventoryService inventoryService;
    private final ReservationCommandOutputPort reservationCommandOutputPort;
    private final CouponOutputPort couponOutputPort;
    private final PaymentOutputPort paymentOutputPort;

    public Long prepareReservation(ReservationForCreate reservationForCreate){
        log.debug("[ReservationProcessingService] [prepareReservation]");
        try {
            // 1. 쿠폰 유효성 검증
            couponOutputPort.verifyCoupon(reservationForCreate.paymentForCreate().appliedCoupon()); // FeignClient

            // 2. 예약 정보 DB 저장 및 예약 번호 반환 (PENDING)
            return reservationCommandOutputPort.createReservation(reservationForCreate).getReservationId();

        } catch (Exception e) {
            log.error("[ReservationProcessingService] [prepareReservation] Error occurred: {}", e.getMessage());
            throw new ReservationProcessingException("예약 준비 중 오류가 발생했습니다.");
        }
    }

    public Payment processPayment(Long reservationId, PaymentForCreate paymentForCreate){
        log.info("[ReservationProcessingService] [processPayment] reservationId ::: {}", reservationId);
        try {
            // 1. Payment 서비스 호출
            return paymentOutputPort.processPayment(paymentForCreate, reservationId); // FeignClient
        } catch (Exception e){
            log.error("[ReservationProcessingService] [processPayment] Error occurred {}: {}", reservationId, e.getMessage());
            // 상태 수정
            reservationCommandOutputPort.updateReservationStatus(reservationId, ReservationStatus.CANCELLED);
            throw new PaymentException("결제가 진행되지 않았습니다.");
        }
    }

    public void confirmReservation(Long reservationId, Payment payment){
        log.info("[ReservationProcessingService] [confirmReservation] reservationId ::: {}", reservationId);
        try{
            // 1. Inventory 수정
            inventoryService.updateTotalReserved(reservationId);

            // 2. 쿠폰 사용 처리
            couponOutputPort.useCoupon(payment.appliedCoupon()); // FeignClient
        }catch (Exception e){
            log.error("[ReservationProcessingService] [confirmReservation] Error occurred {}: {}", reservationId, e.getMessage());
            // 환불 처리
            paymentOutputPort.processRefund(payment.paymentId());
            // 상태 변경
            reservationCommandOutputPort.updateReservationStatus(reservationId, ReservationStatus.REFUNDED);
            throw new ReservationProcessingException("예약 확정 중 오류가 발생했습니다.");
        }
    }
}
