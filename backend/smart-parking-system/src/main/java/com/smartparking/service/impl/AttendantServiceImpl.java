package com.smartparking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.CreateAttendantRequest;
import com.smartparking.dto.response.UserResponse;
import com.smartparking.entity.ParkingProperty;
import com.smartparking.entity.Role;
import com.smartparking.entity.User;
import com.smartparking.exception.BadRequestException;
import com.smartparking.exception.DuplicateResourceException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.ParkingPropertyRepository;
import com.smartparking.repository.RoleRepository;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.AttendantService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendantServiceImpl implements AttendantService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ParkingPropertyRepository parkingPropertyRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	@Override
	public UserResponse createAttendant(CreateAttendantRequest request) {

		User admin = getLoggedInAdmin();

		ParkingProperty property = parkingPropertyRepository
				.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking property not found for logged in admin."));

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already exists.");
		}

		if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
			throw new DuplicateResourceException("Mobile number already exists.");
		}

		Role attendantRole = roleRepository.findByRoleName("ATTENDANT")
				.orElseThrow(() -> new ResourceNotFoundException("Role not found."));

		User attendant = new User();
		attendant.setFirstName(request.getFirstName());
		attendant.setLastName(request.getLastName());
		attendant.setEmail(request.getEmail());
		attendant.setPassword(passwordEncoder.encode(request.getPassword()));
		attendant.setMobileNumber(request.getMobileNumber());
		attendant.setRole(attendantRole);
		attendant.setWorkingProperty(property);
		attendant.setActive(true);

		attendant = userRepository.save(attendant);

		return modelMapper.map(attendant, UserResponse.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getMyAttendants() {

		User admin = getLoggedInAdmin();

		ParkingProperty property = parkingPropertyRepository
				.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking property not found for logged in admin."));

		return userRepository.findByWorkingPropertyAndRole_RoleName(property, "ATTENDANT")
				.stream()
				.map(user -> modelMapper.map(user, UserResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAttendant(Long attendantId) {

		User admin = getLoggedInAdmin();

		ParkingProperty property = parkingPropertyRepository
				.findByAdmin(admin)
				.orElseThrow(() -> new ResourceNotFoundException("Parking property not found for logged in admin."));

		User attendant = userRepository.findById(attendantId)
				.orElseThrow(() -> new ResourceNotFoundException("Attendant not found."));

		if (!"ATTENDANT".equals(attendant.getRole().getRoleName())) {
			throw new BadRequestException("Selected user is not an attendant.");
		}

		if (attendant.getWorkingProperty() == null
				|| !attendant.getWorkingProperty().getPropertyId().equals(property.getPropertyId())) {

			throw new BadRequestException("You are not authorized to delete this attendant.");
		}

		attendant.setActive(false);

		userRepository.save(attendant);
	}

	private User getLoggedInAdmin() {

		User admin = getLoggedInUser();

		if (!"ADMIN".equals(admin.getRole().getRoleName())) {
			throw new BadRequestException("Only admin can perform this operation.");
		}

		return admin;
	}

	private User getLoggedInUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}
}