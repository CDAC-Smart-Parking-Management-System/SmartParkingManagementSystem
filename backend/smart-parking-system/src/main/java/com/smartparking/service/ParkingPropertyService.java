package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.ParkingPropertyRequest;
import com.smartparking.dto.response.ParkingPropertyResponse;

public interface ParkingPropertyService {

	List<ParkingPropertyResponse> getAllProperties();
	
	ParkingPropertyResponse getMyProperty();
	
	ParkingPropertyResponse getPropertyById(Long propertyId);
	
	ParkingPropertyResponse updateProperty(Long propertyId, ParkingPropertyRequest request);
}