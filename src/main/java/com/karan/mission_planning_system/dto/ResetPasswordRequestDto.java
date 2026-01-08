package com.karan.mission_planning_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDto {

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;
}
