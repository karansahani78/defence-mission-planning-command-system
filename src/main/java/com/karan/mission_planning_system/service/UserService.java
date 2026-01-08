package com.karan.mission_planning_system.service;

import com.karan.mission_planning_system.dto.*;
import com.karan.mission_planning_system.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    UserResponseDto registerUser(UserRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    void sendPasswordResetOtp(String email);

    void resetPassword(String otp, String newPassword);

    UserResponseDto getCurrentUserProfile();

    Page<PersonnelSummaryDto> getPersonnelByRole(
            Role role,
            Pageable pageable
    );
}
