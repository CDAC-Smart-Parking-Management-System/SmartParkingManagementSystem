package com.smartparking.dto.request;

import java.math.BigDecimal;

import com.smartparking.enums.VehicleType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRateRequest {

	@NotNull(message = "Vehicle type is required")
	private VehicleType vehicleType;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	private BigDecimal price;
}	