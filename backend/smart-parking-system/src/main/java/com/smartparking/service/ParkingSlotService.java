package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.response.ParkingSlotResponse;

public interface ParkingSlotService {

    ParkingSlotResponse getSlotById(Long slotId);

    List<ParkingSlotResponse> getSlotsByFloor(Long floorId);

    List<ParkingSlotResponse> getAvailableSlots();

    List<ParkingSlotResponse> getAvailableSlotsPublic();
    
    List<ParkingSlotResponse> getAvailableSlotsByProperty(Long propertyId);
    
    List<ParkingSlotResponse> getSlotsByProperty(Long propertyId);

}