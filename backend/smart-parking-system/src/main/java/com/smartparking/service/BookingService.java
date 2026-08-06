package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.BookingRequest;
import com.smartparking.dto.response.BookingResponse;

public interface BookingService {

	BookingResponse bookSlot(BookingRequest request);

	BookingResponse getBookingById(Long bookingId);

	List<BookingResponse> getMyBookings();

	BookingResponse cancelBooking(Long bookingId);
	
	List<BookingResponse> getAllBookings();

}