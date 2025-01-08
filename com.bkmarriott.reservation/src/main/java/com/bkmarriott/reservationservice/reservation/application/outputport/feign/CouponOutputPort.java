package com.bkmarriott.reservationservice.reservation.application.outputport.feign;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;

public interface CouponOutputPort {
    CouponDto verifyCoupon(Long couponId);
    CouponDto useCoupon(Long couponId);

}
