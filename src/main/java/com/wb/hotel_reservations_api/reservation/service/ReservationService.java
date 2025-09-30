package com.wb.hotel_reservations_api.reservation.service;

import com.wb.hotel_reservations_api.reservation.dto.ReservationRequestDTO;
import com.wb.hotel_reservations_api.reservation.model.Reservation;
import com.wb.hotel_reservations_api.reservation.model.ReservationStatus;
import com.wb.hotel_reservations_api.reservation.repository.ReservationRepository;
import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService; // Para buscar o quarto

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    /**
     * ----------------------------------------------------
     * REGRA PRINCIPAL: CRIAÇÃO DE RESERVA (ANTI-OVERBOOKING)
     * ----------------------------------------------------
     */
    private Reservation createReservation(Reservation newReservation) {

        // 1. Validar o Quarto
        Room room = roomService.findById(newReservation.getRoom().getId());

        // 2. Validar o Período e as Datas
        validateReservationDates(newReservation.getCheckInDate(), newReservation.getCheckOutDate());

        // 3. REGRA ANTI-OVERBOOKING (Chamada da Query Mágica!)
        if (isOverlapping(newReservation, room)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Overbooking detectado. O quarto " + room.getRoomNumber() +
                            " já possui uma reserva confirmada para o período solicitado.");
        }

        // 4. Calcular o Preço Total
        BigDecimal total = calculateTotalPrice(room.getPricePerNight(),
                newReservation.getCheckInDate(),
                newReservation.getCheckOutDate());
        newReservation.setTotalPrice(total);

        // 5. Configurar o Status e salvar
        newReservation.setStatus(ReservationStatus.CONFIRMED);
        newReservation.setRoom(room); // Garante que a entidade Room completa está linkada

        return reservationRepository.save(newReservation);
    }

    /**
     * ----------------------------------------------------
     * Método auxiliar de Validação
     * ----------------------------------------------------
     */
    private void validateReservationDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de saída deve ser posterior à data de entrada.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível criar reservas para o passado.");
        }
    }

    /**
     * ----------------------------------------------------
     * LÓGICA ANTI-OVERBOOKING
     * ----------------------------------------------------
     */
    private boolean isOverlapping(Reservation newReservation, Room room) {
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                room.getId(),
                ReservationStatus.CONFIRMED, // Apenas checamos conflitos com reservas CONFIRMADAS
                newReservation.getCheckInDate(),
                newReservation.getCheckOutDate()
        );
        // Se a lista não estiver vazia, há sobreposição!
        return !overlappingReservations.isEmpty();
    }

    /**
     * ----------------------------------------------------
     * LÓGICA DE CÁLCULO
     * ----------------------------------------------------
     */
    private BigDecimal calculateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        // Garante que o número de noites não seja negativo ou zero
        if (numberOfNights <= 0) return BigDecimal.ZERO;

        return pricePerNight.multiply(BigDecimal.valueOf(numberOfNights));
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada com ID: " + id));
    }

    /**
     * Altera o status da reserva para CANCELADA.
     */
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta reserva já foi cancelada.");
        }

        // REGRA: Podemos adicionar validações de política de cancelamento aqui (ex: taxa)
        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAllActive() {
        return reservationRepository.findByStatusIn(List.of(ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation createReservationFromDto(ReservationRequestDTO requestDTO) {

        // Mapear DTO para Entidade
        Reservation newReservation = new Reservation();
        newReservation.setClientName(requestDTO.getClientName());
        newReservation.setClientEmail(requestDTO.getClientEmail());
        newReservation.setCheckInDate(requestDTO.getCheckInDate());
        newReservation.setCheckOutDate(requestDTO.getCheckOutDate());
        newReservation.setNumberOfGuests(requestDTO.getNumberOfGuests());

        // O Room Service busca o Room real, evitando Lazy Load Issue no DTO
        Room room = roomService.findById(requestDTO.getRoomId());
        newReservation.setRoom(room);

        // Chamar o método principal de criação que contém a REGRA DE NEGÓCIO
        return this.createReservation(newReservation);
    }
}
