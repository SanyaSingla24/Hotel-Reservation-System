package com.example.hotel_reservation_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    public Reservation() {}

    public Long getId()                          { return id; }
    public Guest getGuest()                      { return guest; }
    public void setGuest(Guest g)                { this.guest = g; }
    public Room getRoom()                        { return room; }
    public void setRoom(Room r)                  { this.room = r; }
    public LocalDate getCheckInDate()            { return checkInDate; }
    public void setCheckInDate(LocalDate d)      { this.checkInDate = d; }
    public LocalDate getCheckOutDate()           { return checkOutDate; }
    public void setCheckOutDate(LocalDate d)     { this.checkOutDate = d; }
    public BigDecimal getTotalCost()             { return totalCost; }
    public void setTotalCost(BigDecimal t)       { this.totalCost = t; }
    public ReservationStatus getStatus()         { return status; }
    public void setStatus(ReservationStatus s)   { this.status = s; }
    public Payment getPayment()                  { return payment; }
    public void setPayment(Payment p)            { this.payment = p; }
}