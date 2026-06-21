package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.dto.OccupancySummaryDTO;
import com.example.hotel_reservation_system.model.Reservation;
import com.example.hotel_reservation_system.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Long guestId);
    List<Reservation> findByStatus(ReservationStatus status);

    @Query("""
        SELECT new com.example.hotel_reservation_system.dto.OccupancySummaryDTO(
            YEAR(r.checkInDate), MONTH(r.checkInDate),
            COUNT(r), SUM(DATEDIFF(r.checkOutDate, r.checkInDate)),
            SUM(r.totalCost)
        )
        FROM Reservation r
        WHERE r.status <> 'CANCELLED'
        GROUP BY YEAR(r.checkInDate), MONTH(r.checkInDate)
        ORDER BY YEAR(r.checkInDate) DESC, MONTH(r.checkInDate) DESC
    """)
    List<OccupancySummaryDTO> findMonthlyOccupancySummary();

    @Query("""
        SELECT r.room.roomType, SUM(r.totalCost)
        FROM Reservation r
        WHERE r.status = 'CONFIRMED'
        GROUP BY r.room.roomType
    """)
    List<Object[]> findRevenueByRoomType();
}