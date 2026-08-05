package com.smartparking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.smartparking.enums.VehicleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_rates", 
			uniqueConstraints = {
			@UniqueConstraint(columnNames = { "property_id", "vehicle_type" }) 
		})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rate_id")
	private Long rateId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id", nullable = false)
	private ParkingProperty parkingProperty;

	@Enumerated(EnumType.STRING)
	@Column(name = "vehicle_type", nullable = false)
	private VehicleType vehicleType;

	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

}