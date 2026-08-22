package com.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.Booking;
import com.smartparking.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserUserId(Long userId);

	List<Booking> findByParkingSlotSlotId(Long slotId);

	List<Booking> findByBookingStatus(BookingStatus bookingStatus);
	
	List<Booking> findByParkingSlotFloorParkingPropertyAdminUserId(Long adminId);

	boolean existsByParkingSlotSlotIdAndBookingStatusIn(Long slotId, List<BookingStatus> bookingStatuses);
	
	List<Booking> findByParkingSlotFloorParkingPropertyPropertyId(Long propertyId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)

	@Query("""
	        SELECT b
	        FROM Booking b
	        WHERE b.bookingStatus = :status
	        AND b.expectedCheckInTime IS NOT NULL
	        AND b.expectedCheckInTime < :now
	        """)
	List<Booking> findExpiredBookings(
	        @Param("status") BookingStatus status,
	        @Param("now") LocalDateTime now
	);
}