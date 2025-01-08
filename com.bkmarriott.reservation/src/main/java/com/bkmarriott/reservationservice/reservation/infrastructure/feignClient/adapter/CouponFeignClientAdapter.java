package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.feign.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.CouponClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponFeignClientAdapter implements CouponOutputPort {

    private final CouponClient couponClient;

    @Override
    public CouponDto verifyCoupon(Long couponId) {
        log.info("[CouponFeignClientAdapter] [verifyCoupon] couponId ::: {} ", couponId);
        return couponClient.verifyCoupon(couponId);
    }

    @Override
    public CouponDto useCoupon(Long couponId) {
        log.info("[CouponFeignClientAdapter] [useCoupon] couponId ::: {} ", couponId);
        return couponClient.useCoupon(couponId);
    }
}
