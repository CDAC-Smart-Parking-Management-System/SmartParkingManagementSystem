package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.FloorRequest;
import com.smartparking.dto.response.FloorResponse;

public interface FloorService {

    FloorResponse addFloor(FloorRequest floorRequest);

    List<FloorResponse> getAllFloors();

    FloorResponse getFloorById(Long floorId);

    FloorResponse updateFloor(Long floorId, FloorRequest floorRequest);

}