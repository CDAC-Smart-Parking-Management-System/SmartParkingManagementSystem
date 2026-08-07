package com.smartparking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartparking.dto.response.BookingResponse;
import com.smartparking.service.EntryExitService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EntryExitController {

	private final EntryExitService entryExitService;

	@PostMapping("/api/entry/{bookingId}")
	public ResponseEntity<BookingResponse> checkIn(@PathVariable Long bookingId) {

		BookingResponse response = entryExitService.checkIn(bookingId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/api/exit/{bookingId}")
	public ResponseEntity<BookingResponse> checkOut(@PathVariable Long bookingId) {

		BookingResponse response = entryExitService.checkOut(bookingId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}