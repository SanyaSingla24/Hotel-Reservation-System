package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.model.Payment;
import com.example.hotel_reservation_system.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByReservationId(Long reservationId);
    List<Payment> findByPaymentMethod(PaymentMethod method);
}