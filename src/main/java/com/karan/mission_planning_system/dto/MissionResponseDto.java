package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.MissionPriority;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.enums.SecurityLevel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionResponseDto {

    /* ================= IDENTITY ================= */

    private Long id;
    private String missionCode;
    private String missionName;
    private String missionObjective;

    /* ================= CLASSIFICATION ================= */

    private MissionStatus status;
    private MissionPriority priority;
    private SecurityLevel securityLevel;

    /* ================= PLANNING PARAMETERS ================= */

    private Double estimatedDurationHours;
    private Double estimatedRangeKm;
    private Integer minAssetReadiness;
    private Double minRequiredSustainmentLevel;

    /* ================= PLANNED TIMELINE ================= */

    private LocalDateTime startTime;
    private LocalDateTime plannedEndTime;

    /* ================= ACTUAL EXECUTION ================= */

    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    /* ================= OPERATIONAL CONTEXT ================= */

    private String operationArea;
    private String abortReason;

    /* ================= COMMAND & AUDIT ================= */

    private String createdBy;
    private LocalDateTime createdAt;

    private String commander;

    private String approvedBy;
    private LocalDateTime approvedAt;

    private String startedBy;
    private String completedBy;

    private String abortedBy;
    private LocalDateTime abortedAt;

    private LocalDateTime updatedAt;
}
