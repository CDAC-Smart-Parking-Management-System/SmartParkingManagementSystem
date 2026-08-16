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

	// how many minutes the customer needs to arrive and check-in.
	// If they don't check-in within this time, the booking is auto
	// cancelled and the slot is released back to AVAILABLE.
	// Only two options are allowed: 15 minutes or 30 minutes.
	// (the exact-value check for 15/30 is done in BookingServiceImpl)
	@NotNull(message = "Arrival time is required")
	private Integer arrivalMinutes;

}