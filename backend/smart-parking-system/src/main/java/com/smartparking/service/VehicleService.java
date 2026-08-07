package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.VehicleRequest;
import com.smartparking.dto.response.VehicleResponse;

public interface VehicleService {

    VehicleResponse registerVehicle(VehicleRequest request);

    VehicleResponse getVehicleById(Long vehicleId);

    List<VehicleResponse> getMyVehicles();

    VehicleResponse updateVehicle(Long vehicleId, VehicleRequest request);

    void deleteVehicle(Long vehicleId);

}