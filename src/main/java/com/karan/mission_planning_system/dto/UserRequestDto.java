package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    /**
     * Only SYSTEM_ADMIN can assign roles
     */
    private Role role;
}
