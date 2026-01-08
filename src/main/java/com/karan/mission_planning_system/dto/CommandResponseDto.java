package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.CommandStatus;
import com.karan.mission_planning_system.enums.CommandType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandResponseDto {

    private Long id;
    private String commandCode;
    private CommandType commandType;
    private String instruction;

    private Long missionId;
    private String missionCode;

    private Long targetAssetId;
    private String targetAssetCode;

    private String issuedBy;
    private String assignedTo;
    private String executedBy;

    private LocalDateTime issuedAt;
    private LocalDateTime executeBy;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime executedAt;

    private CommandStatus status;
    private SecurityLevel securityLevel;
    private boolean emergency;

    private String failureReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
