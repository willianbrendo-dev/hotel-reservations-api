package com.wb.hotel_reservations_api.reservation.repository;

import com.wb.hotel_reservations_api.reservation.model.Reservation;
import com.wb.hotel_reservations_api.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * REGRA ANTI-OVERBOOKING:
     * * Esta query verifica se o período de uma nova reserva (checkIn, checkOut)
     * se sobrepõe a qualquer reserva EXISTENTE e CONFIRMADA para o mesmo quarto.
     * * A sobreposição ocorre se:
     * 1. A nova data de entrada for ANTES da saída EXISTENTE E
     * 2. A nova data de saída for DEPOIS da entrada EXISTENTE.
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.room.id = :roomId AND r.status = :status AND (" +
            // Condição 1: A nova entrada está no meio da reserva existente
            "(:newCheckIn < r.checkOutDate) AND " +
            // Condição 2: A nova saída está no meio da reserva existente
            "(:newCheckOut > r.checkInDate)" +
            ")")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("status") ReservationStatus status,
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut
    );

    /**
     * Busca todas as reservas ativas (não canceladas e não finalizadas - CHECKED_OUT).
     */
    List<Reservation> findByStatusIn(List<ReservationStatus> statuses);

    /**
     * Busca todas as reservas por um cliente específico.
     */
    List<Reservation> findByClientEmail(String clientEmail);
}
