package com.smartparking.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.ParkingPropertyRequest;
import com.smartparking.dto.response.ParkingPropertyResponse;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.User;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.ParkingPropertyService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingPropertyServiceImpl implements ParkingPropertyService {

	private final ParkingPropertyRepository parkingPropertyRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ParkingPropertyResponse> getAllProperties() {

		return parkingPropertyRepository
				.findAll()
				.stream()
				.map(property -> modelMapper.map(property, ParkingPropertyResponse.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ParkingPropertyResponse getMyProperty() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User admin = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		ParkingProperty property = parkingPropertyRepository.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking property not found."));

		return modelMapper.map(property, ParkingPropertyResponse.class);
	}

	@Override
	@Transactional(readOnly = true)
	public ParkingPropertyResponse getPropertyById(Long propertyId) {

		ParkingProperty property = getLoggedInAdminProperty();

		if (!property.getPropertyId().equals(propertyId)) {
			throw new ResourceNotFoundException("Parking property not found with id : " + propertyId);
		}

		return modelMapper.map(property, ParkingPropertyResponse.class);
	}

	@Override
	public ParkingPropertyResponse updateProperty(Long propertyId, ParkingPropertyRequest request) {

		ParkingProperty property = getLoggedInAdminProperty();

		if (!property.getPropertyId().equals(propertyId)) {
			throw new ResourceNotFoundException("Parking property not found with id : " + propertyId);
		}

		property.setPropertyName(request.getPropertyName());
		property.setAddress(request.getAddress());
		property.setCity(request.getCity());
		property.setTotalFloors(request.getTotalFloors());
		property.setOpeningTime(request.getOpeningTime());
		property.setClosingTime(request.getClosingTime());

		// Saves an entity and flushes changes instantly
		ParkingProperty updatedProperty = parkingPropertyRepository.saveAndFlush(property);

		return modelMapper.map(updatedProperty, ParkingPropertyResponse.class);
	}

	// Helper Methods
	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private ParkingProperty getLoggedInAdminProperty() {

		User admin = getLoggedInUser();

		return parkingPropertyRepository.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking property not found for logged in admin"));
	}
}