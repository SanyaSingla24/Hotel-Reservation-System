package com.example.hotel_reservation_system.controller;
import com.example.hotel_reservation_system.dto.GuestDTO;
import com.example.hotel_reservation_system.model.Guest;
import com.example.hotel_reservation_system.repository.GuestRepository;
import com.example.hotel_reservation_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guests")
@CrossOrigin(origins = "http://localhost:5173")
public class GuestController {

    @Autowired private GuestRepository  guestRepository;
    @Autowired private PasswordEncoder  passwordEncoder;
    @Autowired private JwtUtil          jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody GuestDTO dto) {
        if (guestRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        Guest guest = new Guest();
        guest.setFullName(dto.getFullName());
        guest.setEmail(dto.getEmail());
        guest.setPhone(dto.getPhone());
        guest.setNationalId(dto.getNationalId());
        guest.setPassword(passwordEncoder.encode(dto.getPassword()));  // BCrypt hash
        guestRepository.save(guest);
        return ResponseEntity.ok("Guest registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody GuestDTO dto) {
        return guestRepository.findByEmail(dto.getEmail())
                .map(g -> {
                    if (passwordEncoder.matches(dto.getPassword(), g.getPassword())) {
                        String token = jwtUtil.generateToken(g.getEmail());
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("error", "Guest not found")));
    }

    @GetMapping
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return guestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id,
                                             @RequestBody GuestDTO dto) {
        return guestRepository.findById(id).map(g -> {
            g.setFullName(dto.getFullName());
            g.setEmail(dto.getEmail());
            g.setPhone(dto.getPhone());
            g.setNationalId(dto.getNationalId());
            return ResponseEntity.ok(guestRepository.save(g));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) {
        guestRepository.deleteById(id);
        return ResponseEntity.ok("Guest deleted successfully");
    }
}