package com.karan.mission_planning_system.controller;

import com.karan.mission_planning_system.dto.*;
import com.karan.mission_planning_system.enums.Role;
import com.karan.mission_planning_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ================= AUTH ================= */

    @PostMapping("/register")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(requestDto));
    }

    /* ================= PROFILE ================= */

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getCurrentUserProfile() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUserProfile());
    }

    /* ================= PERSONNEL (DEFENSE-GRADE) ================= */

    /**
     * Defense-grade personnel lookup
     *
     * ✔ Role-filtered
     * ✔ Paginated
     * ✔ Anti-enumeration
     * ✔ Restricted to command authority
     */
    @GetMapping("/personnel")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','MISSION_COMMANDER')")
    public ResponseEntity<Page<PersonnelSummaryDto>> getPersonnelByRole(
            @RequestParam Role role,
            Pageable pageable) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getPersonnelByRole(role, pageable));
    }

    /* ================= PASSWORD RESET ================= */

    @PostMapping("/password/reset-request")
    public ResponseEntity<Void> sendPasswordResetOtp(
            @RequestParam String email) {

        userService.sendPasswordResetOtp(email);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String otp,
            @RequestParam String newPassword) {

        userService.resetPassword(otp, newPassword);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
