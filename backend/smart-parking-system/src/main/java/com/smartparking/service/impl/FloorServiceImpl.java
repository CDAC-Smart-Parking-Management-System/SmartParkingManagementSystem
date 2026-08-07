package com.smartparking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.FloorRequest;
import com.smartparking.dto.response.FloorResponse;
import com.smartparking.entity.Floor;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.User;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotStatus;
import com.smartparking.enums.SlotType;
import com.smartparking.exception.BadRequestException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.FloorRepository;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.FloorService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {

	private final FloorRepository floorRepository;
	private final ParkingPropertyRepository propertyRepository;
	private final ParkingSlotRepository parkingSlotRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public FloorResponse addFloor(FloorRequest floorRequest) {

		ParkingProperty property = getLoggedInAdminProperty();

		// Validate maximum floor limit
		List<Floor> existingFloors = floorRepository.findByParkingProperty(property);

		if (existingFloors.size() >= property.getTotalFloors()) {
			throw new BadRequestException("Maximum floor limit reached. Cannot add more floors.");
		}

		Floor floor = new Floor();
			floor.setFloorName(floorRequest.getFloorName());
			floor.setFloorNumber(existingFloors.size() + 1);
			floor.setCarSlots(floorRequest.getCarSlots());
			floor.setBikeSlots(floorRequest.getBikeSlots());
			floor.setEvSlots(floorRequest.getEvSlots());
			floor.setParkingProperty(property);

		Floor savedFloor = floorRepository.saveAndFlush(floor);

		createSlots(savedFloor, SlotType.CAR, floorRequest.getCarSlots(), "C");
		createSlots(savedFloor, SlotType.BIKE, floorRequest.getBikeSlots(), "B");
		createSlots(savedFloor, SlotType.EV, floorRequest.getEvSlots(), "E");

		return mapToResponse(savedFloor);
	}

	@Override
	public List<FloorResponse> getAllFloors() {

		ParkingProperty property = getLoggedInAdminProperty();

		return floorRepository.findByParkingProperty(property)
				.stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public FloorResponse getFloorById(Long floorId) {

		ParkingProperty property = getLoggedInAdminProperty();

		Floor floor = floorRepository.findByFloorIdAndParkingProperty(floorId, property)
				.orElseThrow(() -> new ResourceNotFoundException("Floor not found with id : " + floorId));

		return mapToResponse(floor);
	}

	@Override
	public FloorResponse updateFloor(Long floorId, FloorRequest floorRequest) {

		ParkingProperty property = getLoggedInAdminProperty();

		Floor floor = floorRepository.findByFloorIdAndParkingProperty(floorId, property)
				.orElseThrow(() -> new ResourceNotFoundException("Floor not found with id : " + floorId));
		
		floor.setFloorName(floorRequest.getFloorName());
		Floor updatedFloor = floorRepository.saveAndFlush(floor);
		return mapToResponse(updatedFloor);
	}
	
	//Helper Methods
	private void createSlots(Floor floor, SlotType slotType, Integer count, String prefix) {

		if (count == null || count == 0)
			return;

		for (int i = 1; i <= count; i++) {

			ParkingSlot slot = new ParkingSlot();
			slot.setFloor(floor);
			slot.setSlotType(slotType);
			slot.setSlotStatus(SlotStatus.AVAILABLE);
			// P1-F1-C-01
			slot.setSlotNumber("P" + floor.getParkingProperty().getPropertyId() + "-" + 
					"F" + floor.getFloorNumber() + "-" + prefix + "-" + String.format("%02d", i));

			parkingSlotRepository.save(slot);
		}
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

	private FloorResponse mapToResponse(Floor floor) {

		FloorResponse response = modelMapper.map(floor, FloorResponse.class);

		response.setCarSlots(floor.getCarSlots());
		response.setBikeSlots(floor.getBikeSlots());
		response.setEvSlots(floor.getEvSlots());
		//manually calculated total and inserted count
		response.setTotalSlots(floor.getCarSlots() + floor.getBikeSlots() + floor.getEvSlots());

		response.setPropertyId(floor.getParkingProperty().getPropertyId());
		response.setPropertyName(floor.getParkingProperty().getPropertyName());

		return response;
	}
}