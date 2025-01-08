package com.bkmarriott.reservationservice.reservation.presentation.rest.util.auth;

import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.auth.Actor;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.auth.Role;
import com.bkmarriott.reservationservice.reservation.presentation.rest.exception.InvalidAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class LoginActorArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_ROLE = "X-Role";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginActor.class) &&
                parameter.getParameterType().equals(Actor.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Long userId = extractUserId(webRequest);
        Role role = extractRole(webRequest);

        return new Actor(userId, role);
    }

    private Long extractUserId(NativeWebRequest webRequest) {
        String userId = webRequest.getHeader(HEADER_USER_ID);
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException exception) {
            log.error("[LoginActorArgumentResolver] [extractUserId] exception ::: ", exception);
            throw new InvalidAccessException("비정상적인 유저 접근");
        }
    }

    private Role extractRole(NativeWebRequest webRequest) {
        String role = webRequest.getHeader(HEADER_ROLE);
        try {
            return Role.valueOf(role);

            // if role mismatch
        } catch (Exception exception) {
            log.error("[LoginActorArgumentResolver] [extractRole] exception ::: ", exception);
            throw new InvalidAccessException("비정상적인 유저 접근");
        }
    }
}
