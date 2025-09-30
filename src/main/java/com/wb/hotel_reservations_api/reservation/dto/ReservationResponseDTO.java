package com.wb.hotel_reservations_api.reservation.dto;

import com.wb.hotel_reservations_api.reservation.model.Reservation;
import com.wb.hotel_reservations_api.reservation.model.ReservationStatus;
import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.model.RoomType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ReservationResponseDTO {
    private Long id;
    private Long roomId; // Expondo apenas o ID do quarto
    private String roomNumber;
    private RoomType roomType;

    private String clientName;
    private String clientEmail;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;
    private BigDecimal totalPrice;
    private int numberOfGuests;

    // Método estático para conversão da Entidade JPA para o DTO
    public static ReservationResponseDTO fromEntity(Reservation reservation) {
        Room room = reservation.getRoom(); // O acesso aqui está seguro dentro de uma transação

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .roomId(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .clientName(reservation.getClientName())
                .clientEmail(reservation.getClientEmail())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .status(reservation.getStatus())
                .totalPrice(reservation.getTotalPrice())
                .numberOfGuests(reservation.getNumberOfGuests())
                .build();
    }
}
