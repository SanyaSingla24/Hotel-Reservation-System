package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.model.*;
import com.example.hotel_reservation_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired private PaymentRepository     paymentRepository;
    @Autowired private ReservationRepository reservationRepository;

    @PostMapping
    public ResponseEntity<Payment> processPayment(
            @RequestParam Long reservationId,
            @RequestParam String paymentMethod) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalCost());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        payment.setTransactionId(UUID.randomUUID().toString());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        return ResponseEntity.ok(paymentRepository.save(payment));
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return paymentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservation/{resId}")
    public ResponseEntity<Payment> getByReservation(@PathVariable Long resId) {
        return paymentRepository.findByReservationId(resId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/method/{method}")
    public List<Payment> getByMethod(@PathVariable String method) {
        return paymentRepository.findByPaymentMethod(
                PaymentMethod.valueOf(method.toUpperCase()));
    }
}