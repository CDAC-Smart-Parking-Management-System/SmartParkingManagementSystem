package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.ParkingRateRequest;
import com.smartparking.dto.response.ParkingRateResponse;

public interface ParkingRateService {

	ParkingRateResponse addParkingRate(ParkingRateRequest request);

	List<ParkingRateResponse> getAllParkingRates();

	ParkingRateResponse getParkingRateById(Long rateId);

	ParkingRateResponse updateParkingRate(Long rateId, ParkingRateRequest request);

}