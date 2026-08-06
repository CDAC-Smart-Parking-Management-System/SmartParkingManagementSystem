package com.smartparking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByMobileNumber(String mobileNumber);

	Optional<User> findByEmail(String email);

	Optional<User> findByMobileNumber(String mobileNumber);

	List<User> findByWorkingProperty(ParkingProperty workingProperty);

	List<User> findByWorkingPropertyAndRole_RoleName(ParkingProperty workingProperty, String roleName);

}