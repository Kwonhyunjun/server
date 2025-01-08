package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bkmarriott.reservationservice.reservation.infrastructure.config.QueryDslConfig;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.config.PersistenceConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import({PersistenceTestConfig.class, QueryDslConfig.class, PersistenceConfig.class})
@ActiveProfiles("test")
public @interface RepositoryTest {

}