package com.smartparking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

	@NotNull(message = "Vehicle Id is required")
	private Long vehicleId;

	@NotNull(message = "Slot Id is required")
	private Long slotId;

}