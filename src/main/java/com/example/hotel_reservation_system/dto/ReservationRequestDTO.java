package com.example.hotel_reservation_system.dto;

import java.time.LocalDate;

public class ReservationRequestDTO {
    private Long guestId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String paymentMethod;

    public Long getGuestId()                   { return guestId; }
    public void setGuestId(Long g)             { this.guestId = g; }
    public Long getRoomId()                    { return roomId; }
    public void setRoomId(Long r)              { this.roomId = r; }
    public LocalDate getCheckInDate()          { return checkInDate; }
    public void setCheckInDate(LocalDate d)    { this.checkInDate = d; }
    public LocalDate getCheckOutDate()         { return checkOutDate; }
    public void setCheckOutDate(LocalDate d)   { this.checkOutDate = d; }
    public String getPaymentMethod()           { return paymentMethod; }
    public void setPaymentMethod(String m)     { this.paymentMethod = m; }
}