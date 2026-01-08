package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonnelSummaryDto {

    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
}
