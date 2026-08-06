package com.smartparking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.User;

public interface ParkingPropertyRepository extends JpaRepository<ParkingProperty, Long> {

	Optional<ParkingProperty> findByAdmin(User admin);

}