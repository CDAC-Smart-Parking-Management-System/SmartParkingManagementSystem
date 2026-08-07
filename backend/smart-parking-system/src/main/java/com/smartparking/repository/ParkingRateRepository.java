package com.smartparking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.ParkingRate;
import com.smartparking.enums.VehicleType;

public interface ParkingRateRepository extends JpaRepository<ParkingRate, Long> {

	Optional<ParkingRate> findByParkingPropertyPropertyIdAndVehicleType(Long propertyId, VehicleType vehicleType);

	List<ParkingRate> findByParkingPropertyPropertyId(Long propertyId);

	boolean existsByParkingPropertyPropertyIdAndVehicleType(Long propertyId, VehicleType vehicleType);

	Optional<ParkingRate> findByRateIdAndParkingPropertyPropertyId(Long rateId, Long propertyId);
}