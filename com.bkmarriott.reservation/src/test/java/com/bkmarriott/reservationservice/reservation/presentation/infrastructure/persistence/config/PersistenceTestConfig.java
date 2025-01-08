package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config;

import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryCommandAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.InventoryQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.ReservationCommandAdapter;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PersistenceTestConfig {

  @Autowired
  private JPAQueryFactory jpaQueryFactory;

  @Bean
  public InventoryQueryDslRepository inventoryQueryDslRepository() {
    return new InventoryQueryDslRepository(jpaQueryFactory);
  }

  @Bean
  public InventoryQueryAdaptor inventoryQueryAdaptor(@Autowired InventoryRepository inventoryRepository,
      InventoryQueryDslRepository inventoryQueryDslRepository) {
    return new InventoryQueryAdaptor(inventoryRepository, inventoryQueryDslRepository);
  }

  @Bean
  public InventoryCommandAdaptor inventoryCommandAdaptor(@Autowired InventoryRepository inventoryRepository) {
    return new InventoryCommandAdaptor(inventoryRepository);
  }

  @Bean
  public ReservationCommandAdapter reservationCommandAdapter(@Autowired ReservationRepository reservationRepository){
    return new ReservationCommandAdapter(reservationRepository);
  }
}
