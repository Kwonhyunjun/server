package com.bkmarriott.reservationservice.reservation.presentation.rest.config;

import com.bkmarriott.reservationservice.reservation.presentation.rest.util.auth.LoginActorArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestConfig implements WebMvcConfigurer {

    private final LoginActorArgumentResolver loginActorArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginActorArgumentResolver);
    }
}
