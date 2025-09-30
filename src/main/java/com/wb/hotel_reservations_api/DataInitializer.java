package com.wb.hotel_reservations_api;

import com.wb.hotel_reservations_api.reservation.model.Reservation;
import com.wb.hotel_reservations_api.reservation.model.ReservationStatus;
import com.wb.hotel_reservations_api.reservation.repository.ReservationRepository;
import com.wb.hotel_reservations_api.room.model.Room;
import com.wb.hotel_reservations_api.room.model.RoomType;
import com.wb.hotel_reservations_api.room.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        return args -> {

            // 1. LIMPA PARA GARANTIR NOVO TESTE (Opcional, mas útil para H2 em memória)
            reservationRepository.deleteAll();
            roomRepository.deleteAll();

            // 2. CRIAÇÃO DE QUARTOS INICIAIS
            Room room101 = new Room("101", RoomType.SINGLE, new BigDecimal("150.00"), 1);
            Room room205 = new Room("205", RoomType.DOUBLE, new BigDecimal("250.00"), 2);
            Room room300 = new Room("300", RoomType.SUITE, new BigDecimal("500.00"), 4);

            List<Room> savedRooms = roomRepository.saveAll(List.of(room101, room205, room300));

            // Garante que a aplicação tem acesso aos IDs gerados
            Room suiteRoom = savedRooms.stream()
                    .filter(r -> r.getRoomNumber().equals("300"))
                    .findFirst().orElseThrow();

            // 3. CRIAÇÃO DE UMA RESERVA CONFIRMADA (Ocupando o Quarto 300)

            // Período Ocupado: 2026-01-10 a 2026-01-15 (5 noites)
            Reservation initialReservation = new Reservation();
            initialReservation.setRoom(suiteRoom);
            initialReservation.setClientName("Alice Teste");
            initialReservation.setClientEmail("alice@test.com");
            initialReservation.setCheckInDate(LocalDate.of(2026, 1, 10));
            initialReservation.setCheckOutDate(LocalDate.of(2026, 1, 15));
            initialReservation.setStatus(ReservationStatus.CONFIRMED);
            initialReservation.setNumberOfGuests(2);

            // Cálculo do preço (5 noites * 500)
            initialReservation.setTotalPrice(new BigDecimal("2500.00"));

            reservationRepository.save(initialReservation);

            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("DADOS INICIAIS CARREGADOS:");
            System.out.println("Quarto 300 (SUITE) está reservado de 2026-01-10 até 2026-01-15.");
            System.out.println("-----------------------------------------------------------------------------------");
        };
    }
}
