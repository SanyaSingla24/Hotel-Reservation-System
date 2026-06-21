package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.dto.RoomDTO;
import com.example.hotel_reservation_system.model.Room;
import com.example.hotel_reservation_system.model.RoomType;
import com.example.hotel_reservation_system.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<Room> addRoom(@RequestBody RoomDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(RoomType.valueOf(dto.getRoomType()));
        room.setPricePerNight(dto.getPricePerNight());
        room.setFloor(dto.getFloor());
        room.setAvailable(true);
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id,
                                           @RequestBody RoomDTO dto) {
        return roomRepository.findById(id).map(r -> {
            r.setRoomNumber(dto.getRoomNumber());
            r.setRoomType(RoomType.valueOf(dto.getRoomType()));
            r.setPricePerNight(dto.getPricePerNight());
            r.setFloor(dto.getFloor());
            r.setAvailable(dto.isAvailable());
            return ResponseEntity.ok(roomRepository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomRepository.deleteById(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}