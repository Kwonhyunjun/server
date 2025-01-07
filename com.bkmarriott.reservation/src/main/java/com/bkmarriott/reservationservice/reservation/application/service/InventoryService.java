package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Response;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryCommandOutputPort inventoryCommandOutputPort;
  private final InventoryQueryOutputPort inventoryQueryOutputPort;
  private final ReservationQueryOutputPort reservationQueryOutputPort;

  public List<Inventory> updateTotalReserved(Long reservationId) {

    Reservation reservation = reservationQueryOutputPort.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 정보"));

    try {

      if (reservation.getStatus().equals(ReservationStatus.PAID)) {
          log.debug("[InventoryService] [Increase totalReserved] hotelId ::: {}, roomtype ::: {}"
              , reservation.getHotelId(), reservation.getRoomType());

          return Inventory.from(reservation)
              .stream()
              .map(inventory -> inventoryCommandOutputPort.increaseReserved(inventory)
                  .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 인벤토리 정보")))
              .toList();
        }

      if (reservation.getStatus().equals(ReservationStatus.CANCELLED) || reservation.getStatus().equals(ReservationStatus.REFUNDED)) {
        log.debug("[InventoryService] [Decrease totalReserved] hotelId ::: {}, roomtype ::: {}"
            , reservation.getHotelId(), reservation.getRoomType());

        return Inventory.from(reservation)
            .stream()
            .map(inventory -> inventoryCommandOutputPort.decreaseReserved(inventory)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 인벤토리 정보")))
            .toList();
        }

      throw new IllegalArgumentException("잘못된 예약 상태 정보");

    } catch (Exception e) {
      log.error("[InventoryService] [updateTotalReserved] hotelId ::: {}, roomType ::: {}",
          reservation.getHotelId(), reservation.getRoomType(), e);
      throw new InventoryUpdateFailureException("객실 예약 인벤토리 정보 수정 실패");
    }

  }

public List<Response> getInventoryQuantity(Long hotelId, LocalDate startDate, LocalDate endDate) {

    List<InventoryQueryResponseDto> availableRooms = inventoryQueryOutputPort
        .findAvailableRoomsByHotelIdAndDateRange(new InventoryQueryRequestDto(
            hotelId,startDate,endDate
        ));

    if (availableRooms == null || availableRooms.isEmpty()) {
      log.warn("[InventoryService] [find Available Rooms] hotelId: {}, startDate: {}, endDate: {}",
          hotelId, startDate, endDate);
      throw new IllegalArgumentException("예약 가능 객실 수량 조회 결과가 없습니다.");
    }

    return availableRooms.stream().map(Response::from).toList();
  }

  public int getAvailableRoomCount(InventoryQuery query) {
    List<Inventory> inventoryFromReservation = inventoryQueryOutputPort.findInventoryFromReservation(query);

    return inventoryFromReservation.stream()
            .mapToInt(Inventory::getAvailableRoomCount)
            .min()
            .orElseThrow(() -> new ResourceNotFoundException("해당 예약정보에 해당하는 객실 정보를 찾을 수 없습니다."));
  }
}
