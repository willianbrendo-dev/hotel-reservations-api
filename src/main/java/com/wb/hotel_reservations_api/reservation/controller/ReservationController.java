package com.wb.hotel_reservations_api.reservation.controller;

import com.wb.hotel_reservations_api.reservation.dto.ReservationRequestDTO;
import com.wb.hotel_reservations_api.reservation.dto.ReservationResponseDTO;
import com.wb.hotel_reservations_api.reservation.model.Reservation;
import com.wb.hotel_reservations_api.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * GET /api/v1/reservations
     * Lista todas as reservas (ativas e finalizadas).
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        // Converte a lista de Entidades para DTOs
        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(ReservationResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/v1/reservations/active
     * Lista todas as reservas ATIVAS (CONFIRMED ou CHECKED_IN).
     */
    @GetMapping("/active")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservations() {
        // Usa o novo método findAllActive no Service, e mapeia
        List<Reservation> reservations = reservationService.findAllActive();
        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(ReservationResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/v1/reservations/{id}
     * Busca uma reserva por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        // Busca a Entidade e retorna o DTO
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(ReservationResponseDTO.fromEntity(reservation));
    }

    /**
     * POST /api/v1/reservations
     * Recebe DTO, Converte para Entidade (no Service) e Retorna DTO.
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO requestDTO) {
        // O Service cuida da lógica e conversão (DTO -> Entidade -> Salva -> Entidade)
        Reservation newReservation = reservationService.createReservationFromDto(requestDTO);

        // Retorna o ResponseDTO
        return new ResponseEntity<>(ReservationResponseDTO.fromEntity(newReservation), HttpStatus.CREATED);
    }

    /**
     * PUT /api/v1/reservations/{id}/cancel
     * Cancela uma reserva ativa.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Long id) {
        // O Service cancela a Entidade, e o Controller retorna o DTO atualizado
        Reservation cancelledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(ReservationResponseDTO.fromEntity(cancelledReservation));
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<ReservationResponseDTO> checkInReservation(@PathVariable Long id) {
        // 1. Chama o Service, que retorna a Entidade atualizada
        Reservation reservation = reservationService.checkInReservation(id);

        // 2. Converte a Entidade para DTO e retorna
        return ResponseEntity.ok(ReservationResponseDTO.fromEntity(reservation));
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<ReservationResponseDTO> checkOutReservation(@PathVariable Long id) {
        // 1. Chama o Service, que retorna a Entidade atualizada
        Reservation reservation = reservationService.checkOutReservation(id);

        // 2. Converte a Entidade para DTO e retorna
        return ResponseEntity.ok(ReservationResponseDTO.fromEntity(reservation));
    }
}
