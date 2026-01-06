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

    private Long id;

    private String missionCode;
    private String missionName;
    private String missionObjective;

    private MissionStatus status;
    private MissionPriority priority;
    private SecurityLevel securityLevel;

    /* Planned timeline */
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /* Actual execution timeline */
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    private String operationArea;
    private String abortReason;

    /* ===== AUDIT INFO (READ ONLY) ===== */

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
