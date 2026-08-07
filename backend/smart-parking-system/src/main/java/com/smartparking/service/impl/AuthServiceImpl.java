package com.smartparking.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartparking.dto.request.LoginRequest;
import com.smartparking.dto.request.RegisterRequest;
import com.smartparking.dto.response.LoginResponse;
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
import com.smartparking.security.CustomUserDetails;
import com.smartparking.security.CustomUserDetailsService;
import com.smartparking.security.JwtUtils;
import com.smartparking.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ParkingPropertyRepository parkingPropertyRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtils jwtUtils;

	@Override
	public UserResponse register(RegisterRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already exists.");
		}

		if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
			throw new DuplicateResourceException("Mobile number already exists.");
		}

		String roleName = request.getRole().toUpperCase();

		if ("ATTENDANT".equals(roleName)) {
			System.out.println("Attendant cannot self register.");
			throw new BadRequestException("Attendant cannot self register.");
		}

		Role role = roleRepository
				.findByRoleName(roleName)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found."));

		User user = new User();
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setEmail(request.getEmail());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setMobileNumber(request.getMobileNumber());
			user.setRole(role);
			user.setActive(true);

		user = userRepository.save(user);

		if ("ADMIN".equals(roleName)) {

			ParkingProperty property = new ParkingProperty();
				property.setPropertyName(request.getPropertyName());
				property.setAddress(request.getAddress());
				property.setCity(request.getCity());
				property.setTotalFloors(request.getTotalFloors());
				property.setOpeningTime(request.getOpeningTime());
				property.setClosingTime(request.getClosingTime());
				property.setActive(true);
				property.setAdmin(user);

			property = parkingPropertyRepository.save(property);

			user.setWorkingProperty(property);
			user = userRepository.save(user);
		}

		UserResponse response = new UserResponse();
			response.setUserId(user.getUserId());
			response.setFirstName(user.getFirstName());
			response.setLastName(user.getLastName());
			response.setEmail(user.getEmail());
			response.setMobileNumber(user.getMobileNumber());
			response.setRole(user.getRole().getRoleName());

		return response;
	}

	@Override
	public LoginResponse login(LoginRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService
				.loadUserByUsername(request.getEmail());

		String token = jwtUtils.generateJwtToken(userDetails);

		User user = userRepository
						.findByEmail(request.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		UserResponse userResponse = new UserResponse();
			userResponse.setUserId(user.getUserId());
			userResponse.setFirstName(user.getFirstName());
			userResponse.setLastName(user.getLastName());
			userResponse.setEmail(user.getEmail());
			userResponse.setMobileNumber(user.getMobileNumber());
			userResponse.setRole(user.getRole().getRoleName());

		LoginResponse response = new LoginResponse();
			response.setToken(token);
			response.setTokenType("Bearer");
			response.setUser(userResponse);

		return response;
	}
}