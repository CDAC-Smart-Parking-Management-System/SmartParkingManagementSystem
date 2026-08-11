package com.smartparking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.response.ParkingSlotResponse;
import com.smartparking.entity.Floor;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.User;
import com.smartparking.enums.SlotStatus;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.FloorRepository;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.ParkingSlotService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

	private final ParkingSlotRepository parkingSlotRepository;
	private final FloorRepository floorRepository;
	private final ParkingPropertyRepository propertyRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public ParkingSlotResponse getSlotById(Long slotId) {

		ParkingProperty property = getLoggedInAdminProperty();
		
		// ensure admin can only access slots belonging to their own property
		Floor floor = floorRepository.findByParkingProperty(property)
				.stream()
				.flatMap(f -> parkingSlotRepository.findByFloor(f).stream())
				.filter(slot -> slot.getSlotId().equals(slotId)).map(ParkingSlot::getFloor).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found with id : " + slotId));

		ParkingSlot parkingSlot = parkingSlotRepository.findBySlotIdAndFloor(slotId, floor)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Slot not found with id : " + slotId));

		return mapToResponse(parkingSlot);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParkingSlotResponse> getSlotsByFloor(Long floorId) {

		ParkingProperty property = getLoggedInAdminProperty();

		Floor floor = floorRepository.findByFloorIdAndParkingProperty(floorId, property)
				.orElseThrow(() -> new ResourceNotFoundException("Floor not found with id : " + floorId));

		return parkingSlotRepository.findByFloor(floor)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParkingSlotResponse> getAvailableSlots() {
//		System.out.println("Inside getAvailableSlots()");

		ParkingProperty property = getLoggedInAdminProperty();

		List<Floor> floors = floorRepository.findByParkingProperty(property);

		return floors.stream().flatMap(floor -> parkingSlotRepository.findByFloor(floor).stream())
				.filter(slot -> slot.getSlotStatus() == SlotStatus.AVAILABLE).map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	// Simple version with no admin/property restriction - just returns
	// every AVAILABLE slot in the whole system. Used by the chatbot and
	// any other customer-facing screen where the caller is not an admin.
	@Override
	@Transactional(readOnly = true)
	public List<ParkingSlotResponse> getAvailableSlotsPublic() {

		return parkingSlotRepository.findAll().stream()
				.filter(slot -> slot.getSlotStatus() == SlotStatus.AVAILABLE)
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	// not used now - changed with below method
	@Override
	@Transactional(readOnly = true)
	public List<ParkingSlotResponse> getAvailableSlotsByProperty(Long propertyId) {

		ParkingProperty property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Property not found"));

		return parkingSlotRepository.findByFloorParkingPropertyAndSlotStatus(property, SlotStatus.AVAILABLE)
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParkingSlotResponse> getSlotsByProperty(Long propertyId) {

		ParkingProperty property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Property not found"));

		List<Floor> floors = floorRepository.findByParkingProperty(property);

		return floors.stream().flatMap(floor -> parkingSlotRepository.findByFloor(floor).stream())
				.map(this::mapToResponse).collect(Collectors.toList());
	}

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

	private ParkingSlotResponse mapToResponse(ParkingSlot parkingSlot) {

		ParkingSlotResponse response = modelMapper.map(parkingSlot, ParkingSlotResponse.class);

		response.setFloorId(parkingSlot.getFloor().getFloorId());
		response.setFloorName(parkingSlot.getFloor().getFloorName());

		return response;
	}
}