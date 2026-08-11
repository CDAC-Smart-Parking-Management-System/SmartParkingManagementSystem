package com.smartparking.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.ParkingRateRequest;
import com.smartparking.dto.response.ParkingRateResponse;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.ParkingRate;
import com.smartparking.entity.User;
import com.smartparking.exception.DuplicateResourceException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.ParkingRateRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.ParkingRateService;

import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ParkingRateServiceImpl implements ParkingRateService {

	private final ParkingRateRepository parkingRateRepository;
	private final ParkingPropertyRepository propertyRepository;
	private final UserRepository userRepository;

	private final ModelMapper modelMapper;

	@Override
	public ParkingRateResponse addParkingRate(ParkingRateRequest request) {

		ParkingProperty property = getLoggedInAdminProperty();

		// as we used @UniqueConstraint(columnNames = { "property_id", "vehicle_type" }
		if (parkingRateRepository
				.existsByParkingPropertyPropertyIdAndVehicleType (property.getPropertyId(),
						request.getVehicleType())) {

			throw new DuplicateResourceException("Parking rate already exists for this vehicle type");
		}

		ParkingRate parkingRate = new ParkingRate();

		parkingRate.setParkingProperty(property);
		parkingRate.setVehicleType(request.getVehicleType());
		parkingRate.setPrice(request.getPrice());

		ParkingRate savedRate = parkingRateRepository.save(parkingRate);

		ParkingRateResponse response = modelMapper.map(savedRate, ParkingRateResponse.class);

		response.setPropertyId(property.getPropertyId());
		response.setPropertyName(property.getPropertyName());

		return response;
	}

	@Override
	public List<ParkingRateResponse> getAllParkingRates() {

		ParkingProperty property = getLoggedInAdminProperty();

		return parkingRateRepository
				.findByParkingPropertyPropertyId(property.getPropertyId())
				.stream()
				.map(rate -> {
					ParkingRateResponse response = modelMapper.map(rate, ParkingRateResponse.class);
					response.setPropertyId(property.getPropertyId());
					response.setPropertyName(property.getPropertyName());
					return response;
				}).toList();
		}

	// Simple version with no admin/property restriction - just returns
	// every parking rate in the whole system. Used by the chatbot and
	// any other customer-facing screen where the caller is not an admin.
	@Override
	public List<ParkingRateResponse> getAllParkingRatesPublic() {

		return parkingRateRepository.findAll()
				.stream()
				.map(rate -> {
					ParkingRateResponse response = modelMapper.map(rate, ParkingRateResponse.class);
					response.setPropertyId(rate.getParkingProperty().getPropertyId());
					response.setPropertyName(rate.getParkingProperty().getPropertyName());
					return response;
				}).toList();
	}

	@Override
	public ParkingRateResponse getParkingRateById(Long rateId) {

		ParkingProperty property = getLoggedInAdminProperty();

		ParkingRate rate = parkingRateRepository
				.findByRateIdAndParkingPropertyPropertyId(rateId, property.getPropertyId())
				.orElseThrow(() -> new ResourceNotFoundException("Parking Rate not found"));

		ParkingRateResponse response = modelMapper.map(rate, ParkingRateResponse.class);

		response.setPropertyId(property.getPropertyId());
		response.setPropertyName(property.getPropertyName());

		return response;
	}

	@Override
	public ParkingRateResponse updateParkingRate(Long rateId, ParkingRateRequest request) {

		ParkingProperty property = getLoggedInAdminProperty();

		ParkingRate rate = parkingRateRepository
				.findByRateIdAndParkingPropertyPropertyId(rateId, property.getPropertyId())
				.orElseThrow(() -> new ResourceNotFoundException("Parking Rate not found"));

		rate.setPrice(request.getPrice());

		ParkingRate updatedRate = parkingRateRepository.save(rate);

		ParkingRateResponse response = modelMapper.map(updatedRate, ParkingRateResponse.class);

		response.setPropertyId(property.getPropertyId());
		response.setPropertyName(property.getPropertyName());

		return response;
	}
	
	// Helper Method
	
	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private ParkingProperty getLoggedInAdminProperty() {

		User admin = getLoggedInUser();

		return propertyRepository.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Property not found for logged in admin"));
	}

}