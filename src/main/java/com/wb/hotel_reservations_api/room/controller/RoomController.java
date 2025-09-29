package com.wb.hotel_reservations_api.room.controller;

import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.model.RoomType;
import com.wb.hotel_reservations_api.room.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * GET /api/v1/rooms
     * Lista todos os quartos.
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    /**
     * GET /api/v1/rooms/{id}
     * Busca um quarto por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    /**
     * GET /api/v1/rooms/available
     * Lista todos os quartos disponíveis.
     */
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.findAvailableRooms());
    }

    /**
     * GET /api/v1/rooms/type/{roomType}
     * Busca quartos por tipo (ex: SUITE).
     */
    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable RoomType roomType) {
        return ResponseEntity.ok(roomService.findRoomsByType(roomType));
    }

    /**
     * POST /api/v1/rooms
     * Cria um novo quarto.
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room newRoom = roomService.saveOrUpdate(room);
        // Retorna 201 Created
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    /**
     * PUT /api/v1/rooms/{id}
     * Atualiza um quarto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        // Garantir que o ID do caminho seja usado para a atualização
        roomDetails.setId(id);
        Room updatedRoom = roomService.saveOrUpdate(roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    /**
     * DELETE /api/v1/rooms/{id}
     * Deleta um quarto por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        // Retorna 204 No Content para indicar sucesso na deleção
        return ResponseEntity.noContent().build();
    }
}
