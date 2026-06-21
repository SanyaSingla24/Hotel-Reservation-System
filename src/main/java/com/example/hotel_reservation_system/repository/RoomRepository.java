package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.model.Room;
import com.example.hotel_reservation_system.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByAvailable(boolean available);

    @Query("""
        SELECT r FROM Room r WHERE r.available = true
        AND r.id NOT IN (
            SELECT res.room.id FROM Reservation res
            WHERE res.status <> 'CANCELLED'
            AND res.checkInDate  < :checkOut
            AND res.checkOutDate > :checkIn
        )
    """)
    List<Room> findAvailableRooms(
            @Param("checkIn")  LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}