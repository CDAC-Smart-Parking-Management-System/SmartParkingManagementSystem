package com.smartparking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.Floor;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotStatus;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

	List<ParkingSlot> findByFloorFloorId(Long floorId);

	List<ParkingSlot> findBySlotStatus(SlotStatus slotStatus);

	List<ParkingSlot> findByFloor(Floor floor);

	Optional<ParkingSlot> findBySlotIdAndFloor(Long slotId, Floor floor);

	boolean existsByFloorAndSlotNumber(Floor floor, String slotNumber);

	List<ParkingSlot> findByFloorParkingPropertyAndSlotStatus(ParkingProperty parkingProperty, SlotStatus slotStatus);

}