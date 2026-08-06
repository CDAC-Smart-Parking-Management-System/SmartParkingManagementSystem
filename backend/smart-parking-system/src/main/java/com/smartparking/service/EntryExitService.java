package com.smartparking.service;

import com.smartparking.dto.response.BookingResponse;

public interface EntryExitService {

	BookingResponse checkIn(Long bookingId);

	BookingResponse checkOut(Long bookingId);

}