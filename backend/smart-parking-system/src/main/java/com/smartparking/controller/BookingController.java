package com.smartparking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartparking.dto.request.BookingRequest;
import com.smartparking.dto.response.BookingResponse;
import com.smartparking.service.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookingResponse bookSlot(@RequestBody @Valid BookingRequest request) {

		return bookingService.bookSlot(request);
	}

	@GetMapping("/{bookingId}")
	public BookingResponse getBookingById(@PathVariable Long bookingId) {

		return bookingService.getBookingById(bookingId);
	}

	@GetMapping("/my")
	public List<BookingResponse> getMyBookings() {

		return bookingService.getMyBookings();
	}

	@GetMapping
	public List<BookingResponse> getAllBookings() {

		return bookingService.getAllBookings();
	}

	@PutMapping("/{bookingId}/cancel")
	public BookingResponse cancelBooking(@PathVariable Long bookingId) {

		return bookingService.cancelBooking(bookingId);
	}

}