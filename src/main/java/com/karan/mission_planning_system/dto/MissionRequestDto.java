package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.MissionPriority;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionRequestDto {

    @NotBlank(message = "Mission code is required")
    @Size(max = 50)
    private String missionCode;

    @NotBlank(message = "Mission name is required")
    @Size(max = 150)
    private String missionName;

    @Size(max = 1000)
    private String missionObjective;

    @NotNull(message = "Mission priority is required")
    private MissionPriority priority;

    @NotNull(message = "Security level is required")
    private SecurityLevel securityLevel;

    @NotNull(message = "Planned start time is required")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Size(max = 200)
    private String operationArea;

    /**
     * Optional:
     * Assign commander during planning stage
     */
    private Long commanderId;
}
