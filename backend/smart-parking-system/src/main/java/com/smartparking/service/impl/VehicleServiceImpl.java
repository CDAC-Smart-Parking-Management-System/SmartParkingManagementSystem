package com.smartparking.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.VehicleRequest;
import com.smartparking.dto.response.VehicleResponse;
import com.smartparking.entity.User;
import com.smartparking.entity.Vehicle;
import com.smartparking.exception.DuplicateResourceException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.UserRepository;
import com.smartparking.repository.VehicleRepository;
import com.smartparking.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

	private final VehicleRepository vehicleRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public VehicleResponse registerVehicle(VehicleRequest request) {

		if (vehicleRepository.existsByVehicleNumber(request.getVehicleNumber())) {
			throw new DuplicateResourceException("Vehicle number already exists.");
		}

		User user = getLoggedInUser();

		Vehicle vehicle = new Vehicle();
			vehicle.setVehicleNumber(request.getVehicleNumber());
			vehicle.setVehicleType(request.getVehicleType());
			vehicle.setVehicleModel(request.getVehicleModel());
			vehicle.setUser(user);

		vehicle = vehicleRepository.save(vehicle);

		return modelMapper.map(vehicle, VehicleResponse.class);
	}

	@Override
	@Transactional(readOnly = true)
	public VehicleResponse getVehicleById(Long vehicleId) {

		User user = getLoggedInUser();

		Vehicle vehicle = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new ResourceNotFoundException("Vehicle not found.");
		}

		return modelMapper.map(vehicle, VehicleResponse.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VehicleResponse> getMyVehicles() {

		User user = getLoggedInUser();

		return vehicleRepository.findByUserUserId(user.getUserId())
				.stream()
				.map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class))
				.toList();
	}

	@Override
	public VehicleResponse updateVehicle(Long vehicleId, VehicleRequest request) {

		User user = getLoggedInUser();

		Vehicle vehicle = vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new ResourceNotFoundException("Vehicle not found.");
		}

		if (!vehicle.getVehicleNumber().equals(request.getVehicleNumber())
				&& vehicleRepository.existsByVehicleNumber(request.getVehicleNumber())) {

			throw new DuplicateResourceException("Vehicle number already exists.");
		}

		vehicle.setVehicleNumber(request.getVehicleNumber());
		vehicle.setVehicleType(request.getVehicleType());
		vehicle.setVehicleModel(request.getVehicleModel());

		vehicle = vehicleRepository.save(vehicle);

		return modelMapper.map(vehicle, VehicleResponse.class);
	}

	@Override
	public void deleteVehicle(Long vehicleId) {

		User user = getLoggedInUser();

		Vehicle vehicle = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));

		if (!vehicle.getUser().getUserId().equals(user.getUserId())) {
			throw new ResourceNotFoundException("Vehicle not found.");
		}

		vehicleRepository.delete(vehicle);
	}

	// Helper Method
	
	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}
}