package com.wb.hotel_reservations_api.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wb.hotel_reservations_api.room.model.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Dados do Quarto ---

    // O Quarto é a chave estrangeira. Usamos @ManyToOne pois um quarto pode ter
    // muitas reservas (em datas diferentes).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // --- Período da Reserva ---
    @Column(nullable = false)
    private LocalDate checkInDate; // Data de entrada

    @Column(nullable = false)
    private LocalDate checkOutDate; // Data de saída

    // --- Dados do Cliente ---

    private String clientName;
    private String clientEmail;

    // --- Status e Valores ---

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    private BigDecimal totalPrice; // Preço final da estadia

    // Outros campos úteis
    private LocalDate createdAt = LocalDate.now();
    private int numberOfGuests;
}
