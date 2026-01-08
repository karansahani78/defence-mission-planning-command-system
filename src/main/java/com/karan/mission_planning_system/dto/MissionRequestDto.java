package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.MissionPriority;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionRequestDto {

    /* ================= IDENTITY ================= */

    @NotBlank(message = "Mission code is required")
    @Size(max = 50)
    private String missionCode;

    @NotBlank(message = "Mission name is required")
    @Size(max = 150)
    private String missionName;

    @Size(max = 1000)
    private String missionObjective;

    /* ================= CLASSIFICATION ================= */

    @NotNull(message = "Mission priority is required")
    private MissionPriority priority;

    @NotNull(message = "Security level is required")
    private SecurityLevel securityLevel;

    /* ================= PLANNING PARAMETERS ================= */

    @Positive
    private Double estimatedDurationHours;

    @Positive
    private Double estimatedRangeKm;

    @Min(0)
    @Max(100)
    private Integer minAssetReadiness; // default handled in service

    @PositiveOrZero
    private Double minRequiredSustainmentLevel;

    /* ================= SCHEDULING ================= */

    @NotNull(message = "Planned start time is required")
    private LocalDateTime startTime;

    private LocalDateTime plannedEndTime;

    /* ================= OPERATIONAL CONTEXT ================= */

    @Size(max = 200)
    private String operationArea;

    /* ================= COMMAND ================= */

    /**
     * Optional: assign commander during planning
     */
    private Long commanderId;
}
