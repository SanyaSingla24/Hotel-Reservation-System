package com.example.hotel_reservation_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(unique = true)
    private String transactionId;

    public Payment() {}

    public Long getId()                              { return id; }
    public Reservation getReservation()              { return reservation; }
    public void setReservation(Reservation r)        { this.reservation = r; }
    public BigDecimal getAmount()                    { return amount; }
    public void setAmount(BigDecimal a)              { this.amount = a; }
    public LocalDateTime getPaymentDate()            { return paymentDate; }
    public void setPaymentDate(LocalDateTime d)      { this.paymentDate = d; }
    public PaymentMethod getPaymentMethod()          { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod m)    { this.paymentMethod = m; }
    public String getTransactionId()                 { return transactionId; }
    public void setTransactionId(String t)           { this.transactionId = t; }
}