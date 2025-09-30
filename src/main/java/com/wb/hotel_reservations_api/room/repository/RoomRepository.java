package com.wb.hotel_reservations_api.room.repository;

import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    /**
     * Retorna todos os quartos com um tipo específico (ex: "SUITE").
     */
    List<Room> findByRoomType(RoomType roomType);

    /**
     * Retorna todos os quartos que estão marcados como disponíveis.
     */
    List<Room> findByIsAvailableTrue();

    /**
     * Retorna todos os quartos disponíveis para o período de check-in e check-out fornecidos.
     * * Implementa a lógica Anti-Overlap (NOT IN) com a condição universal de sobreposição.
     */
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "  SELECT res.room.id FROM Reservation res " +
            "  WHERE res.status IN ('CONFIRMED', 'CHECKED_IN') " + // Status que indicam que o quarto está ocupado
            "  AND (" +
            // CONDIÇÃO UNIVERSAL DE SOBREPOSIÇÃO:
            // A reserva existente termina APÓS o período de busca começar
            "    res.checkOutDate > :checkInDate AND " +
            // E o período de busca termina APÓS a reserva existente começar
            "    :checkOutDate > res.checkInDate " +
            "  )" +
            ")")
    List<Room> findAvailableRooms(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
}
