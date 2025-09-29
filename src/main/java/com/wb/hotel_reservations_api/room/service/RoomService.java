package com.wb.hotel_reservations_api.room.service;

import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.model.RoomType;
import com.wb.hotel_reservations_api.room.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    // Injeção de Dependência via Construtor
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Retorna todos os quartos cadastrados.
     */
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    /**
     * Busca um quarto pelo ID, lançando exceção se não encontrado.
     */
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com ID: " + id));
    }

    /**
     * Cria ou atualiza um quarto.
     */
    public Room saveOrUpdate(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Deleta um quarto por ID.
     */
    public void deleteById(Long id) {
        // Busca primeiro para garantir que o quarto exista antes de tentar deletar
        Room room = findById(id);

        // Futuramente, adicionaremos uma REGRA DE NEGÓCIO aqui:
        // if (room.hasActiveReservations()) { throw exception... }

        roomRepository.delete(room);
    }

    /**
     * Busca todos os quartos que estão disponíveis.
     */
    public List<Room> findAvailableRooms() {
        return roomRepository.findByIsAvailableTrue();
    }

    /**
     * Busca quartos por um tipo específico (ex: "SUITE").
     */
    public List<Room> findRoomsByType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }
}
