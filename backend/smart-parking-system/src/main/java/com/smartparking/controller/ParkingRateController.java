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

import com.smartparking.dto.request.ParkingRateRequest;
import com.smartparking.dto.response.ParkingRateResponse;
import com.smartparking.service.ParkingRateService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/parking-rates")
@AllArgsConstructor
public class ParkingRateController {

	private final ParkingRateService parkingRateService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ParkingRateResponse addParkingRate(@RequestBody @Valid ParkingRateRequest request) {

		return parkingRateService.addParkingRate(request);
	}

	@GetMapping
	public List<ParkingRateResponse> getAllParkingRates() {

		return parkingRateService.getAllParkingRates();
	}

	@GetMapping("/{rateId}")
	public ParkingRateResponse getParkingRateById(@PathVariable Long rateId) {

		return parkingRateService.getParkingRateById(rateId);
	}

	@PutMapping("/{rateId}")
	public ParkingRateResponse updateParkingRate(@PathVariable Long rateId,
			@Valid @RequestBody ParkingRateRequest request) {

		return parkingRateService.updateParkingRate(rateId, request);
	}

}