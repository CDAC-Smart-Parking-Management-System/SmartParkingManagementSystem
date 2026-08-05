package com.smartparking.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "property_id")
	private Long propertyId;

	@Column(name = "property_name", nullable = false, length = 100)
	private String propertyName;

	@Column(nullable = false, length = 255)
	private String address;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(name = "total_floors", nullable = false)
	private int totalFloors;

	@Column(name = "opening_time", nullable = false)
	private LocalTime openingTime;

	@Column(name = "closing_time", nullable = false)
	private LocalTime closingTime;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", unique = true)
	private User admin;

	@OneToMany(mappedBy = "parkingProperty")
	private Set<Floor> floors;

	@OneToMany(mappedBy = "parkingProperty")
	private Set<ParkingRate> parkingRates;

	@OneToMany(mappedBy = "workingProperty")
	private Set<User> attendants;

}