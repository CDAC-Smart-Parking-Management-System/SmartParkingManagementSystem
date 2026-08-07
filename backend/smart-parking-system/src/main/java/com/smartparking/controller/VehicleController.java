package com.smartparking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartparking.dto.request.VehicleRequest;
import com.smartparking.dto.response.VehicleResponse;
import com.smartparking.service.VehicleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

	private final VehicleService vehicleService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleResponse registerVehicle(@RequestBody @Valid VehicleRequest request) {

		return vehicleService.registerVehicle(request);
	}

	@GetMapping("/{vehicleId}")
	public VehicleResponse getVehicleById(@PathVariable Long vehicleId) {

		return vehicleService.getVehicleById(vehicleId);
	}

	@GetMapping
	public List<VehicleResponse> getMyVehicles() {

		return vehicleService.getMyVehicles();
	}

	@PutMapping("/{vehicleId}")
	public VehicleResponse updateVehicle(@PathVariable Long vehicleId, @RequestBody @Valid VehicleRequest request) {

		return vehicleService.updateVehicle(vehicleId, request);
	}

	@DeleteMapping("/{vehicleId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteVehicle(@PathVariable Long vehicleId) {

		vehicleService.deleteVehicle(vehicleId);
	}

}