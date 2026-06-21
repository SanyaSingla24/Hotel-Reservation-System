package com.example.hotel_reservation_system.dto;

import java.math.BigDecimal;

public class OccupancySummaryDTO {
    private int year;
    private int month;
    private long totalReservations;
    private long totalNightsBooked;
    private BigDecimal totalRevenue;

    public OccupancySummaryDTO() {}

    public OccupancySummaryDTO(int year, int month, long totalReservations,
                               long totalNightsBooked, BigDecimal totalRevenue) {
        this.year              = year;
        this.month             = month;
        this.totalReservations = totalReservations;
        this.totalNightsBooked = totalNightsBooked;
        this.totalRevenue      = totalRevenue;
    }

    public int getYear()                         { return year; }
    public void setYear(int y)                   { this.year = y; }
    public int getMonth()                        { return month; }
    public void setMonth(int m)                  { this.month = m; }
    public long getTotalReservations()           { return totalReservations; }
    public void setTotalReservations(long t)     { this.totalReservations = t; }
    public long getTotalNightsBooked()           { return totalNightsBooked; }
    public void setTotalNightsBooked(long n)     { this.totalNightsBooked = n; }
    public BigDecimal getTotalRevenue()          { return totalRevenue; }
    public void setTotalRevenue(BigDecimal r)    { this.totalRevenue = r; }
}