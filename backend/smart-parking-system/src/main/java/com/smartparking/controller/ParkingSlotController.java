package com.smartparking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartparking.dto.response.ParkingSlotResponse;
import com.smartparking.service.ParkingSlotService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ParkingSlotController {

	private final ParkingSlotService parkingSlotService;

	@GetMapping("/{slotId}")
	public ParkingSlotResponse getSlotById(@PathVariable Long slotId) {

		return parkingSlotService.getSlotById(slotId);
	}

	@GetMapping("/floor")
	public List<ParkingSlotResponse> getSlotsByFloor(@RequestParam Long floorId) {

		return parkingSlotService.getSlotsByFloor(floorId);
	}

	@GetMapping("/available")
	public List<ParkingSlotResponse> getAvailableSlots() {

		return parkingSlotService.getAvailableSlots();
	}
	
	@GetMapping("/property/{propertyId}")
	public List<ParkingSlotResponse> getSlotsByProperty(@PathVariable Long propertyId) {

	    return parkingSlotService.getSlotsByProperty(propertyId);

	}

	@GetMapping("/available/property/{propertyId}")
	public List<ParkingSlotResponse> getAvailableSlotsByProperty(@PathVariable Long propertyId) {

		return parkingSlotService.getAvailableSlotsByProperty(propertyId);
	}

}