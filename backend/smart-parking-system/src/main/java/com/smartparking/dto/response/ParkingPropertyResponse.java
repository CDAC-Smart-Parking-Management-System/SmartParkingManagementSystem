package com.smartparking.dto.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingPropertyResponse {

	private Long propertyId;

	private String propertyName;

	private String address;

	private String city;

	private Integer totalFloors;

	private LocalTime openingTime;

	private LocalTime closingTime;

	private boolean isActive;

}