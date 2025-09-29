package com.wb.hotel_reservations_api.room.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms") // Nome da tabela no banco
@Data // Gera Getters, Setters, toString, equals, hashCode (Lombok)
@NoArgsConstructor // Construtor vazio (necessário para JPA)
@AllArgsConstructor // Construtor com todos os argumentos
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único do quarto

    private String roomNumber; // Ex: "101", "205"

    // Tipo do quarto: "SINGLE", "DOUBLE", "SUITE"
    private String roomType;

    // Preço base da diária
    private BigDecimal pricePerNight;

    private int capacity; // Capacidade máxima de pessoas

    private boolean isAvailable; // Flag simples para disponibilidade (ajudará nas buscas iniciais)

    // Construtor parcial para facilitar a criação de dados de teste (opcional)
    public Room(String roomNumber, String roomType, BigDecimal pricePerNight, int capacity) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.isAvailable = true; // Por padrão, o quarto está disponível ao ser criado
    }
}
