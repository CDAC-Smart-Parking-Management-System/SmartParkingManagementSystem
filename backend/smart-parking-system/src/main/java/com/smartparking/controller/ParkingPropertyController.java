package com.smartparking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.smartparking.dto.request.ParkingPropertyRequest;
import com.smartparking.dto.response.ParkingPropertyResponse;
import com.smartparking.service.ParkingPropertyService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // swagger
public class ParkingPropertyController {

	private final ParkingPropertyService parkingPropertyService;

	@GetMapping("/all")
	public List<ParkingPropertyResponse> getAllProperties() {

		return parkingPropertyService.getAllProperties();
	}
	
	@GetMapping
	public ParkingPropertyResponse getMyProperty() { 

		return parkingPropertyService.getMyProperty();
	}

	@GetMapping("/{propertyId}")
	public ParkingPropertyResponse getPropertyById(@PathVariable Long propertyId) {

		return parkingPropertyService.getPropertyById(propertyId);
	}

	@PutMapping("/{propertyId}")
	public ParkingPropertyResponse updateProperty(@PathVariable Long propertyId,
			@Valid @RequestBody ParkingPropertyRequest request) {

		return parkingPropertyService.updateProperty(propertyId, request);
	}
}