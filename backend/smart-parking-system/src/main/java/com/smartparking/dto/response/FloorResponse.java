package com.smartparking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorResponse {

    private Long floorId;

    private String floorName;

	private Integer floorNumber;

    private Long propertyId;

    private String propertyName;
    
    private Integer carSlots;

    private Integer bikeSlots;

    private Integer evSlots;

    private Integer totalSlots;

}