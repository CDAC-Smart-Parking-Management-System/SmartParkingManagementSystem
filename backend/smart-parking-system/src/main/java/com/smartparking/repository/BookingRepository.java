package com.smartparking.repository;

import java.util.List;

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
}