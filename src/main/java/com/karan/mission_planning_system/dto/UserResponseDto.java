package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;
    private boolean accountNonLocked;
    private LocalDateTime createdAt;
}
