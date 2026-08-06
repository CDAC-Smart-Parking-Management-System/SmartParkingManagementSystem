package com.smartparking.dto.response;

import com.smartparking.enums.SlotStatus;
import com.smartparking.enums.SlotType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotResponse {

    private Long slotId;

    private String slotNumber;

    private SlotType slotType;

    private SlotStatus slotStatus;

    private Long floorId;

    private String floorName;

}