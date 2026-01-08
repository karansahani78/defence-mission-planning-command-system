package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.*;
import com.karan.mission_planning_system.entity.User;
import com.karan.mission_planning_system.enums.Role;
import com.karan.mission_planning_system.exception.DuplicateResourceException;
import com.karan.mission_planning_system.exception.UserNotFoundException;
import com.karan.mission_planning_system.repository.UserRepository;
import com.karan.mission_planning_system.security.JwtTokenProvider;
import com.karan.mission_planning_system.security.SecurityUtil;
import com.karan.mission_planning_system.service.EmailService;
import com.karan.mission_planning_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUtil securityUtil;
    private final EmailService emailService;

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDto) {

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(
                        requestDto.getRole() != null
                                ? requestDto.getRole()
                                : Role.ROLE_OPERATIONS_ANALYST
                )
                .enabled(true)
                .accountNonLocked(true)
                .build();

        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() ->
                        new IllegalStateException("Invalid username or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());

        return LoginResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void sendPasswordResetOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email"));

        String otp = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);

        user.setPasswordResetOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.send(
                user.getEmail(),
                "Password Reset OTP",
                "Your OTP is: " + otp + " (valid for 10 minutes)"
        );
    }

    @Override
    public void resetPassword(String otp, String newPassword) {

        User user = userRepository.findByPasswordResetOtp(otp)
                .orElseThrow(() ->
                        new IllegalStateException("Invalid OTP"));

        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetOtp(null);
        user.setOtpExpiryTime(null);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUserProfile() {
        return mapToResponse(securityUtil.getCurrentUser());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonnelSummaryDto> getPersonnelByRole(
            Role role,
            Pageable pageable
    ) {
        User requester = securityUtil.getCurrentUser();

        if (requester.getRole() != Role.ROLE_SYSTEM_ADMIN &&
                requester.getRole() != Role.ROLE_MISSION_COMMANDER) {

            throw new IllegalStateException("Access denied");
        }

        int safeSize = Math.min(pageable.getPageSize(), 50);
        Pageable safePageable =
                PageRequest.of(pageable.getPageNumber(), safeSize);

        return userRepository
                .findByRoleAndEnabledTrue(role, safePageable)
                .map(user -> PersonnelSummaryDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .enabled(user.isEnabled())
                        .build()
                );
    }

    private UserResponseDto mapToResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
