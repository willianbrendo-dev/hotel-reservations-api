package com.wb.hotel_reservations_api.room.repository;

import com.wb.hotel_reservations_api.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    /**
     * Retorna todos os quartos com um tipo específico (ex: "SUITE").
     */
    List<Room> findByRoomType(String roomType);

    /**
     * Retorna todos os quartos que estão marcados como disponíveis.
     */
    List<Room> findByIsAvailableTrue();

    // O Spring Data JPA já nos fornece:
    // - save(Room room)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
}
