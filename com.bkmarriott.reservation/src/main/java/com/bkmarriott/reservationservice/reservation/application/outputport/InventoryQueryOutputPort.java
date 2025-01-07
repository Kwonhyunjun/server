package com.bkmarriott.reservationservice.reservation.application.outputport;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryQueryOutputPort {

  Optional<Inventory> findById(Long hotelId, LocalDate date, RoomType roomType);

  List<InventoryQueryResponseDto> findAvailableRoomsByHotelIdAndDateRange(
      InventoryQueryRequestDto inventoryQueryRequestDto);

  List<Inventory> findInventoryFromReservation(InventoryQuery query);
}
