package com.example.hotel_reservation_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    private int floor;

    @Column(nullable = false)
    private boolean available = true;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public Room() {}

    public Long getId()                              { return id; }
    public String getRoomNumber()                    { return roomNumber; }
    public void setRoomNumber(String n)              { this.roomNumber = n; }
    public RoomType getRoomType()                    { return roomType; }
    public void setRoomType(RoomType t)              { this.roomType = t; }
    public BigDecimal getPricePerNight()             { return pricePerNight; }
    public void setPricePerNight(BigDecimal p)       { this.pricePerNight = p; }
    public int getFloor()                            { return floor; }
    public void setFloor(int f)                      { this.floor = f; }
    public boolean isAvailable()                     { return available; }
    public void setAvailable(boolean a)              { this.available = a; }
    public List<Reservation> getReservations()       { return reservations; }
    public void setReservations(List<Reservation> r) { this.reservations = r; }
}