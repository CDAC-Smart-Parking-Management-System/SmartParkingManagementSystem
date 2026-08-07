package com.smartparking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorRequest {

    @NotBlank(message = "Floor name is required")
    @Size(max = 50)
    private String floorName;
    
    @PositiveOrZero(message = "Car slots cannot be negative")
    private Integer carSlots;

    @PositiveOrZero(message = "Bike slots cannot be negative")
    private Integer bikeSlots;

    @PositiveOrZero(message = "EV slots cannot be negative")
    private Integer evSlots;
}