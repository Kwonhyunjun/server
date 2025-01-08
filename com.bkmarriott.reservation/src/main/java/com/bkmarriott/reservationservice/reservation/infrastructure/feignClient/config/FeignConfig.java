package com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.client")
public class FeignConfig {
}
