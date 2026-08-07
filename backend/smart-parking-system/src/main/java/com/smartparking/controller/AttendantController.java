package com.smartparking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartparking.dto.request.CreateAttendantRequest;
import com.smartparking.dto.response.UserResponse;
import com.smartparking.service.AttendantService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendants")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class AttendantController {

	private final AttendantService attendantService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse createAttendant(@Valid @RequestBody CreateAttendantRequest request) {

		return attendantService.createAttendant(request);
	}

	@GetMapping
	public List<UserResponse> getMyAttendants() {
		return attendantService.getMyAttendants();
	}

	@DeleteMapping("/{attendantId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAttendant(@PathVariable Long attendantId) {

		attendantService.deleteAttendant(attendantId);
	}
}