package com.smartparking.dto.response;

import com.smartparking.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long vehicleId;

    private String vehicleNumber;

    private VehicleType vehicleType;

    private String vehicleModel;

}