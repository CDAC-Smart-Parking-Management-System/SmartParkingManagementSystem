package com.smartparking.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.client.LoggingClient;
import com.smartparking.dto.response.BookingResponse;
import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.User;
import com.smartparking.enums.BookingStatus;
import com.smartparking.enums.PaymentStatus;
import com.smartparking.enums.SlotStatus;
import com.smartparking.exception.BadRequestException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.EntryExitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryExitServiceImpl implements EntryExitService {

	private final BookingRepository bookingRepository;
	private final ParkingSlotRepository parkingSlotRepository;
	private final UserRepository userRepository;
	private final ParkingPropertyRepository propertyRepository;
	private final ModelMapper modelMapper;
	private final LoggingClient loggingClient;

	@Override
	public BookingResponse checkIn(Long bookingId) {

		User loggedInUser = getLoggedInUser();

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		validatePropertyAccess(loggedInUser, booking);

		if (booking.getBookingStatus() != BookingStatus.BOOKED) {
			throw new BadRequestException("Booking is not in BOOKED status");
		}

		booking.setBookingStatus(BookingStatus.ACTIVE);
		booking.setCheckInTime(LocalDateTime.now());

		ParkingSlot slot = booking.getParkingSlot();
		slot.setSlotStatus(SlotStatus.OCCUPIED);

		parkingSlotRepository.save(slot);

		Booking updatedBooking = bookingRepository.save(booking);

		loggingClient.log("VEHICLE_CHECK_IN",
				"Vehicle " + booking.getVehicle().getVehicleNumber() + " checked in on slot "
						+ slot.getSlotNumber(),
				booking.getUser().getEmail());

		return mapToResponse(updatedBooking);
	}

	@Override
	public BookingResponse checkOut(Long bookingId) {

		User loggedInUser = getLoggedInUser();

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		validatePropertyAccess(loggedInUser, booking);

		if (booking.getBookingStatus() != BookingStatus.ACTIVE) {
			throw new BadRequestException("Booking is not ACTIVE");
		}

		booking.setBookingStatus(BookingStatus.COMPLETED);
		booking.setCheckOutTime(LocalDateTime.now());
		booking.setPaymentStatus(PaymentStatus.PAID);

		ParkingSlot slot = booking.getParkingSlot();
		slot.setSlotStatus(SlotStatus.AVAILABLE);

		parkingSlotRepository.save(slot);

		Booking updatedBooking = bookingRepository.save(booking);

		loggingClient.log("VEHICLE_CHECK_OUT",
				"Vehicle " + booking.getVehicle().getVehicleNumber() + " checked out from slot "
						+ slot.getSlotNumber() + ". Amount charged: " + booking.getTotalAmount(),
				booking.getUser().getEmail());

		return mapToResponse(updatedBooking);
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	// Helper method

	private void validatePropertyAccess(User loggedInUser, Booking booking) {

		String role = loggedInUser.getRole().getRoleName();

		ParkingProperty bookingProperty = booking.getParkingSlot().getFloor().getParkingProperty();

		if ("ADMIN".equals(role)) {

			ParkingProperty adminProperty = propertyRepository.findByAdmin(loggedInUser)
					.orElseThrow(() -> new ResourceNotFoundException("Parking Property not found"));

			if (!adminProperty.getPropertyId().equals(bookingProperty.getPropertyId())) {

				throw new BadRequestException("You are not authorized to access this booking.");
			}

		} else if ("ATTENDANT".equals(role)) {

			ParkingProperty workingProperty = loggedInUser.getWorkingProperty();

			if (workingProperty == null || !workingProperty.getPropertyId().equals(bookingProperty.getPropertyId())) {

				throw new BadRequestException("You are not authorized to access this booking.");
			}

		} else {

			throw new BadRequestException("Only Admin or Attendant can perform this operation.");
		}
	}

	private BookingResponse mapToResponse(Booking booking) {

		BookingResponse response = modelMapper.map(booking, BookingResponse.class);

		response.setVehicleNumber(booking.getVehicle().getVehicleNumber());

		response.setSlotNumber(booking.getParkingSlot().getSlotNumber());

		return response;
	}
}