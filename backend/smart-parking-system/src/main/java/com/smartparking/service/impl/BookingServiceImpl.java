package com.smartparking.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.client.LoggingClient;
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
	private final LoggingClient loggingClient;

	@Override
	public BookingResponse bookSlot(BookingRequest request) {

		User user = getLoggedInUser();

		Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new BadRequestException("Vehicle does not belong to the logged-in user");
		}

		ParkingSlot slot = parkingSlotRepository.findBySlotId(request.getSlotId())
		        .orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found"));

		if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
			throw new BadRequestException("Parking Slot is not available");
		}

		if (bookingRepository.existsByParkingSlotSlotIdAndBookingStatusIn(slot.getSlotId(),
				List.of(BookingStatus.BOOKED, BookingStatus.ACTIVE))) {

			throw new BadRequestException("Parking Slot is already booked");
		}

		// Only two check-in windows are allowed: 15 minutes or 30 minutes.
		if (request.getArrivalMinutes() != 15 && request.getArrivalMinutes() != 30) {
			throw new BadRequestException("Check-in time must be either 15 or 30 minutes");
		}

		Booking booking = new Booking();

		ParkingRate parkingRate = parkingRateRepository
				.findByParkingPropertyPropertyIdAndVehicleType(slot.getFloor().getParkingProperty().getPropertyId(),
						vehicle.getVehicleType())
				.orElseThrow(() -> new ResourceNotFoundException("Parking rate not found"));

		booking.setBookingNumber("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		booking.setBookingTime(LocalDateTime.now());
		booking.setExpectedCheckInTime(LocalDateTime.now().plusMinutes(request.getArrivalMinutes()));
		booking.setTotalAmount(parkingRate.getPrice());
		booking.setBookingStatus(BookingStatus.BOOKED);
		booking.setPaymentStatus(PaymentStatus.PENDING);
		booking.setUser(user);
		booking.setVehicle(vehicle);
		booking.setParkingSlot(slot);

		slot.setSlotStatus(SlotStatus.RESERVED);
		parkingSlotRepository.save(slot);

		Booking savedBooking = bookingRepository.save(booking);

		loggingClient.log("BOOKING_CREATED",
				"Booking " + savedBooking.getBookingNumber() + " created for slot " + slot.getSlotNumber()
						+ " with a " + request.getArrivalMinutes() + " minute check-in window",
				user.getEmail());

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

		loggingClient.log("BOOKING_CANCELLED",
				"Booking " + updatedBooking.getBookingNumber() + " was cancelled by the customer", user.getEmail());

		return mapToResponse(updatedBooking);
	}

	// Runs every 1 minute and cancels bookings where the customer never
	// checked in within the 15/30 minute window they chose while booking
	// (a "no-show"), so the slot does not stay reserved forever and goes
	// back to AVAILABLE for other customers.
	@Scheduled(fixedRate = 60 * 1000)
	@Transactional
	public void releaseNoShowBookings() {

	    LocalDateTime now = LocalDateTime.now();

	    List<Booking> bookings =
	            bookingRepository.findExpiredBookings(
	                    BookingStatus.BOOKED,
	                    now
	            );

	    for (Booking booking : bookings) {

	        booking.setBookingStatus(BookingStatus.CANCELLED);

	        ParkingSlot slot = booking.getParkingSlot();

	        if (slot != null) {
	            slot.setSlotStatus(SlotStatus.AVAILABLE);
	        }

	        loggingClient.log(
	                "BOOKING_AUTO_EXPIRED",
	                "Booking " + booking.getBookingNumber()
	                        + " auto-cancelled - vehicle did not check-in on time. "
	                        + "Slot " + slot.getSlotNumber()
	                        + " released.",
	                booking.getUser().getEmail()
	        );
	    }
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