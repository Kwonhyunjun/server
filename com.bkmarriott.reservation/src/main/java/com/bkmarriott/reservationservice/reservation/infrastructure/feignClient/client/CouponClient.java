package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="coupon-service")
public interface CouponClient {

    @PostMapping("/api/vi/coupons/user-coupons/{id}")
    CouponDto verifyCoupon(@PathVariable Long id);

    @PutMapping("/api/vi/coupons/user-coupons/{id}")
    CouponDto useCoupon(@PathVariable Long id);
}
