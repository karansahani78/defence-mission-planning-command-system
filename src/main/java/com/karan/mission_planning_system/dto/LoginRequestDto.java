package com.karan.mission_planning_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
