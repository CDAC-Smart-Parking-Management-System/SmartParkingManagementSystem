package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.ParkingRateRequest;
import com.smartparking.dto.response.ParkingRateResponse;

public interface ParkingRateService {

	ParkingRateResponse addParkingRate(ParkingRateRequest request);

	List<ParkingRateResponse> getAllParkingRates();

	// Used by the chatbot (and any customer-facing screen) - returns
	// every parking rate across ALL properties, without requiring the
	// caller to be an admin who owns a property. getAllParkingRates()
	// above is scoped to the logged-in admin's own property, which is
	// correct for the admin dashboard but not usable for a customer.
	List<ParkingRateResponse> getAllParkingRatesPublic();

	ParkingRateResponse getParkingRateById(Long rateId);

	ParkingRateResponse updateParkingRate(Long rateId, ParkingRateRequest request);

}