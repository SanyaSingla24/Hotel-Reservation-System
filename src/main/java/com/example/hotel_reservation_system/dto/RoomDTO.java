package com.example.hotel_reservation_system.dto;

import java.math.BigDecimal;

public class RoomDTO {
    private Long id;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private int floor;
    private boolean available;

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public String getRoomNumber()              { return roomNumber; }
    public void setRoomNumber(String r)        { this.roomNumber = r; }
    public String getRoomType()                { return roomType; }
    public void setRoomType(String t)          { this.roomType = t; }
    public BigDecimal getPricePerNight()       { return pricePerNight; }
    public void setPricePerNight(BigDecimal p) { this.pricePerNight = p; }
    public int getFloor()                      { return floor; }
    public void setFloor(int f)                { this.floor = f; }
    public boolean isAvailable()               { return available; }
    public void setAvailable(boolean a)        { this.available = a; }
}
