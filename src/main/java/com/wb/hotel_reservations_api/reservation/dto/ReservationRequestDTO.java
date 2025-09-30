package com.wb.hotel_reservations_api.reservation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {
    // O cliente só precisa nos dizer o ID do quarto que deseja
    private Long roomId;

    private String clientName;
    private String clientEmail;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
}
