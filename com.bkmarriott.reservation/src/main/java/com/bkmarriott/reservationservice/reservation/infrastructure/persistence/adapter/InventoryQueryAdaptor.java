package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryQueryAdaptor implements InventoryQueryOutputPort {

  private final InventoryRepository inventoryRepository;
  private final InventoryQueryDslRepository inventoryQueryDslRepository;

  @Override
  public Optional<Inventory> findById(Long hotelId, LocalDate date,
      RoomType roomType) {
    return inventoryRepository.findById(RoomTypeInventoryId.of(hotelId, date,roomType))
        .map(RoomTypeInventoryEntity::toDomain);
  }

  @Override
  public List<InventoryQueryResponseDto> findAvailableRoomsByHotelIdAndDateRange(
      InventoryQueryRequestDto inventoryQueryRequestDto) {
    return inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(inventoryQueryRequestDto);
  }

  @Override
  public List<Inventory> findInventoryFromReservation(InventoryQuery query) {
    return inventoryQueryDslRepository.findInventoryFromReservation(query).stream().map(RoomTypeInventoryEntity::toDomain).toList();
  }
}
