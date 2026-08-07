package com.smartparking.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.BookingRequest;
import com.smartparking.dto.response.BookingResponse;
import com.smartparking.entity.Booking;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.User;
import com.smartparking.entity.Vehicle;
import com.smartparking.entity.ParkingRate;
import com.smartparking.enums.BookingStatus;
import com.smartparking.enums.PaymentStatus;
import com.smartparking.enums.SlotStatus;
import com.smartparking.exception.BadRequestException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.repository.VehicleRepository;
import com.smartparking.repository.ParkingRateRepository;
import com.smartparking.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final VehicleRepository vehicleRepository;
	private final ParkingSlotRepository parkingSlotRepository;
	private final ParkingRateRepository parkingRateRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public BookingResponse bookSlot(BookingRequest request) {

		User user = getLoggedInUser();

		Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new BadRequestException("Vehicle does not belong to the logged-in user");
		}

		ParkingSlot slot = parkingSlotRepository.findById(request.getSlotId())
				.orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found"));

		if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
			throw new BadRequestException("Parking Slot is not available");
		}

		if (bookingRepository.existsByParkingSlotSlotIdAndBookingStatusIn(slot.getSlotId(),
				List.of(BookingStatus.BOOKED, BookingStatus.ACTIVE))) {

			throw new BadRequestException("Parking Slot is already booked");
		}

		Booking booking = new Booking();

		ParkingRate parkingRate = parkingRateRepository
				.findByParkingPropertyPropertyIdAndVehicleType(slot.getFloor().getParkingProperty().getPropertyId(),
						vehicle.getVehicleType())
				.orElseThrow(() -> new ResourceNotFoundException("Parking rate not found"));

		booking.setBookingNumber("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		booking.setBookingTime(LocalDateTime.now());
		booking.setTotalAmount(parkingRate.getPrice());
		booking.setBookingStatus(BookingStatus.BOOKED);
		booking.setPaymentStatus(PaymentStatus.PENDING);
		booking.setUser(user);
		booking.setVehicle(vehicle);
		booking.setParkingSlot(slot);

		slot.setSlotStatus(SlotStatus.RESERVED);
		parkingSlotRepository.save(slot);

		Booking savedBooking = bookingRepository.save(booking);

		return mapToResponse(savedBooking);
	}

	@Override
	@Transactional(readOnly = true)
	public BookingResponse getBookingById(Long bookingId) {

		User user = getLoggedInUser();

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (!booking.getUser().getUserId().equals(user.getUserId())) {
			throw new BadRequestException("Unauthorized access.");
		}

		return mapToResponse(booking);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookingResponse> getMyBookings() {

		User user = getLoggedInUser();

		List<Booking> bookings = bookingRepository.findByUserUserId(user.getUserId());

		return bookings.stream().map(this::mapToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookingResponse> getAllBookings() {

	    User loggedInUser = getLoggedInUser();

	    List<Booking> bookings;

	    String role = loggedInUser.getRole().getRoleName();

	    if ("ADMIN".equals(role)) {

	        bookings = bookingRepository
	                .findByParkingSlotFloorParkingPropertyAdminUserId(loggedInUser.getUserId());

	    } else if ("ATTENDANT".equals(role)) {

	        if (loggedInUser.getWorkingProperty() == null) {
	            throw new ResourceNotFoundException("Working property not assigned.");
	        }

	        bookings = bookingRepository
	                .findByParkingSlotFloorParkingPropertyPropertyId(
	                        loggedInUser.getWorkingProperty().getPropertyId());

	    } else {

	        throw new BadRequestException("Unauthorized access.");

	    }

	    return bookings.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	public BookingResponse cancelBooking(Long bookingId) {

		User user = getLoggedInUser();

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (!booking.getUser().getUserId().equals(user.getUserId())) {
			throw new BadRequestException("You can cancel only your own booking.");
		}

		if (booking.getBookingStatus() != BookingStatus.BOOKED) {
			throw new BadRequestException("Only booked slots can be cancelled");
		}

		booking.setBookingStatus(BookingStatus.CANCELLED);

		ParkingSlot slot = booking.getParkingSlot();
		slot.setSlotStatus(SlotStatus.AVAILABLE);

		parkingSlotRepository.save(slot);

		Booking updatedBooking = bookingRepository.save(booking);

		return mapToResponse(updatedBooking);
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private BookingResponse mapToResponse(Booking booking) {

		BookingResponse response = modelMapper.map(booking, BookingResponse.class);

		response.setVehicleNumber(booking.getVehicle().getVehicleNumber());
		response.setSlotNumber(booking.getParkingSlot().getSlotNumber());

		return response;
	}
}