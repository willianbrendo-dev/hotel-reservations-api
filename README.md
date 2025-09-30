# 🏨 Hotel Reservations API - Documentação Técnica

Projeto Back-end do sistema de reservas de hotel, implementando a gestão de quartos e o ciclo de vida completo da reserva (incluindo anti-overbooking).

---

## 🛠️ Tecnologias e Configuração

* **Linguagem:** Java 21+
* **Framework:** Spring Boot 3+ (Spring Web, JPA)
* **Banco de Dados:** H2 (Em memória, com dados iniciais carregados via DataInitializer).
* **Rodar:** `./mvnw spring-boot:run` (Aplicações inicia na porta 8080).

---

## 🔑 Regras de Negócio Chave

| Regra | Detalhe | Classes Envolvidas |
| :--- | :--- | :--- |
| **Anti-Overbooking** | Uma nova reserva é negada (`409 CONFLICT`) se o quarto tiver qualquer reserva com status `CONFIRMED` ou `CHECKED_IN` que se sobreponha ao período solicitado. | `ReservationRepository`, `ReservationService` |
| **Disponibilidade** | A busca por quartos disponíveis utiliza uma query JPQL complexa (`NOT IN`) para listar apenas os quartos que não possuem sobreposição de reservas ativas no período. | `RoomRepository`, `RoomService` |
| **Ciclo de Vida** | O `CHECK_IN` só pode ocorrer em reservas `CONFIRMED`. O `CHECK_OUT` só pode ocorrer em reservas `CHECKED_IN`. | `ReservationService` |
| **DTOs** | Todas as requisições (`POST`) e respostas (`GET`, `PUT`) no Controller da Reserva utilizam DTOs (`ReservationRequestDTO`, `ReservationResponseDTO`). | `ReservationController` |

---

## 🧭 Endpoints da API

### I. Rotas de Quartos (`/api/v1/rooms`)

| Método | Rota | Descrição | Parâmetros/Body | Status de Sucesso |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Lista todos os quartos. | Nenhum | 200 OK |
| `GET` | `/{id}` | Busca quarto por ID. | Path Variable: `id` (Long) | 200 OK / 404 NOT FOUND |
| `GET` | `/available` | **Busca Avançada:** Lista quartos disponíveis. | Query Params: `checkInDate=YYYY-MM-DD`, `checkOutDate=YYYY-MM-DD` | 200 OK |
| `POST` | `/` | Cria um novo quarto. | Body: `{roomNumber: "305", roomType: "SUITE", ...}` | 201 CREATED |

### II. Rotas de Reservas (`/api/v1/reservations`)

| Método | Rota | Descrição | Parâmetros/Body | Status de Sucesso |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Lista todas as reservas (DTOs). | Nenhum | 200 OK |
| `GET` | `/{id}` | Busca reserva por ID. | Path Variable: `id` (Long) | 200 OK / 404 NOT FOUND |
| `POST` | `/` | Cria nova reserva. | Body: `ReservationRequestDTO` | 201 CREATED |
| `PUT` | `/{id}/checkin` | **Ação:** Altera status para CHECKED_IN. | Path Variable: `id` (Long) | 200 OK / 409 CONFLICT |
| `PUT` | `/{id}/checkout` | **Ação:** Altera status para CHECKED_OUT. | Path Variable: `id` (Long) | 200 OK / 409 CONFLICT |
| `PUT` | `/{id}/cancel` | **Ação:** Altera status para CANCELLED. | Path Variable: `id` (Long) | 200 OK / 404 NOT FOUND |

### Estrutura do Body (POST /api/v1/reservations)

Exemplo de `ReservationRequestDTO` para criar uma reserva:

```json
{
  "roomId": 3,
  "clientName": "João da Silva",
  "clientEmail": "joao@exemplo.com",
  "checkInDate": "2026-03-01",
  "checkOutDate": "2026-03-05",
  "numberOfGuests": 2
}
