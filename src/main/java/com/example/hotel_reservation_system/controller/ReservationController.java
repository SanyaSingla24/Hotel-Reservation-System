package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.dto.OccupancySummaryDTO;
import com.example.hotel_reservation_system.dto.ReservationRequestDTO;
import com.example.hotel_reservation_system.model.*;
import com.example.hotel_reservation_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private GuestRepository       guestRepository;
    @Autowired private RoomRepository        roomRepository;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationRequestDTO dto) {
        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        BigDecimal totalCost = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setTotalCost(totalCost);
        reservation.setStatus(ReservationStatus.PENDING);
        room.setAvailable(false);
        roomRepository.save(room);
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guest/{guestId}")
    public List<Reservation> getByGuest(@PathVariable Long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateStatus(@PathVariable Long id,
                                                    @RequestParam String status) {
        return reservationRepository.findById(id).map(r -> {
            r.setStatus(ReservationStatus.valueOf(status.toUpperCase()));
            if (status.equalsIgnoreCase("CANCELLED")) {
                r.getRoom().setAvailable(true);
                roomRepository.save(r.getRoom());
            }
            return ResponseEntity.ok(reservationRepository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationRepository.findById(id).ifPresent(r -> {
            r.getRoom().setAvailable(true);
            roomRepository.save(r.getRoom());
        });
        reservationRepository.deleteById(id);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }

    @GetMapping("/report/monthly")
    public List<OccupancySummaryDTO> getMonthlySummary() {
        return reservationRepository.findMonthlyOccupancySummary();
    }

    @GetMapping("/report/room-types")
    public Map<String, Double> getRevenueByRoomType() {
        Map<String, Double> result = new HashMap<>();
        reservationRepository.findRevenueByRoomType()
                .forEach(row -> result.put(row[0].toString(),
                        ((BigDecimal) row[1]).doubleValue()));
        return result;
    }
}