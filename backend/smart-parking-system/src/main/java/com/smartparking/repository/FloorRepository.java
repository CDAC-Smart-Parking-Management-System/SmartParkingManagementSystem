package com.smartparking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.Floor;
import com.smartparking.entity.ParkingProperty;

public interface FloorRepository extends JpaRepository<Floor, Long> {

	List<Floor> findByParkingProperty(ParkingProperty parkingProperty);
	
	List<Floor> findByParkingPropertyPropertyId(Long propertyId);

	Optional<Floor> findByFloorIdAndParkingProperty(Long floorId, ParkingProperty parkingProperty);
}