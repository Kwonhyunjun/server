package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.application.exception.RoomTypeInventoryEntityException;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "m_room_type_inventory")
@Entity
public class RoomTypeInventoryEntity extends BaseEntity {

  @EmbeddedId
  private RoomTypeInventoryId id;

  @Column(name = "total_inventory", nullable = false)
  private int totalInventory;

  @Column(name = "total_reserved", nullable = false)
  private int totalReserved;

  @Version
  private Integer version;

  public Inventory toDomain() {
    return Inventory.builder()
        .hotelId(id.getHotelId())
        .date(id.getDate())
        .roomType(id.getRoomType().toDomain())
        .totalInventory(totalInventory)
        .totalReserved(totalReserved)
        .build();
  }

  public RoomTypeInventoryEntity increaseReserved() {

    if(totalInventory == totalReserved) {
      throw new RoomTypeInventoryEntityException("예약 가능한 객실이 없습니다.");
    }

    this.totalReserved++;
    return this;
  }

  public RoomTypeInventoryEntity decreaseReserved() {

    if(totalReserved == 0) {
      throw new RoomTypeInventoryEntityException("예약된 객실이 없습니다.");
    }

    this.totalReserved--;
    return this;
  }
}
