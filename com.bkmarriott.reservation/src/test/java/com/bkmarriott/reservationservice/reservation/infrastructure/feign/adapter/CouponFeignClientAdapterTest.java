package com.bkmarriott.reservationservice.reservation.infrastructure.feign.adapter;

import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.adapter.CouponFeignClientAdapter;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client.CouponClient;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.CouponDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Infrastructure] CouponFeignClientAdapterTest Unit Test")
public class CouponFeignClientAdapterTest {

    @InjectMocks CouponFeignClientAdapter couponFeignClientAdapter;
    @Mock CouponClient couponClient;

    @Test
    @DisplayName("[성공] 쿠폰 검증 FeignClient 테스트 - 쿠폰 ID 로 쿠폰 유효성 검사를 진행하고 해당 쿠폰 정보를 반환한다.")
    public void verifyCoupon_successTest(){
        // Given
        Long couponId = 1L;
        CouponDto mockCoupon = new CouponDto(couponId, 1L, null, null, null);

        Mockito.when(couponClient.verifyCoupon(couponId)).thenReturn(mockCoupon);

        // When
        CouponDto result = couponFeignClientAdapter.verifyCoupon(couponId);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.id(), mockCoupon.id())
        );
    }

    @Test
    @DisplayName("[성공] 쿠폰 사용 FeignClient 테스트 - 쿠폰 ID 로 쿠폰 사용을 진행하고 해당 쿠폰 정보를 반환한다.")
    public void useCoupon_successTest(){
        // Given
        Long couponId = 1L;
        CouponDto mockCoupon = new CouponDto(couponId, 1L, null, null, null);

        Mockito.when(couponClient.useCoupon(couponId)).thenReturn(mockCoupon);

        // When
        CouponDto result = couponFeignClientAdapter.useCoupon(couponId);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.id(), mockCoupon.id())
        );
    }

}
