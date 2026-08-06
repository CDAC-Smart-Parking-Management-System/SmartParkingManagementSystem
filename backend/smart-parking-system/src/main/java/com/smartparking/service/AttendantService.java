package com.smartparking.service;

import java.util.List;

import com.smartparking.dto.request.CreateAttendantRequest;
import com.smartparking.dto.response.UserResponse;

public interface AttendantService {

    UserResponse createAttendant(CreateAttendantRequest request);

    List<UserResponse> getMyAttendants();

    void deleteAttendant(Long attendantId);
}