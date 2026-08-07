package com.smartparking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smartparking.enums.BookingStatus;
import com.smartparking.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long bookingId;

    private String bookingNumber;

    private LocalDateTime bookingTime;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private BigDecimal totalAmount;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private String vehicleNumber;

    private String slotNumber;

}