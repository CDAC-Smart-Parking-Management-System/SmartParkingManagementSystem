package com.smartparking.dto.response;

import java.math.BigDecimal;

import com.smartparking.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRateResponse {

    private Long rateId;

    private Long propertyId;

    private String propertyName;

    private VehicleType vehicleType;

    private BigDecimal price;

}