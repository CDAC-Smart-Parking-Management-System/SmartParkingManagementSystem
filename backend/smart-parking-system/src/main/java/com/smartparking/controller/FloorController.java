package com.smartparking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartparking.dto.request.FloorRequest;
import com.smartparking.dto.response.FloorResponse;
import com.smartparking.service.FloorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FloorController {

    private final FloorService floorService;

    @PostMapping
    public FloorResponse addFloor(@Valid @RequestBody FloorRequest floorRequest) {
        return floorService.addFloor(floorRequest);
    }

    @GetMapping
    public List<FloorResponse> getAllFloors() {
        return floorService.getAllFloors();
    }

    @GetMapping("/{floorId}")
    public FloorResponse getFloorById(@PathVariable Long floorId) {
        return floorService.getFloorById(floorId);
    }

    @PutMapping("/{floorId}")
    public FloorResponse updateFloor(@PathVariable Long floorId,
            @Valid @RequestBody FloorRequest floorRequest) {

        return floorService.updateFloor(floorId, floorRequest);
    }

}