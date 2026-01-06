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

    private String issuedBy;
    private String assignedTo;

    private LocalDateTime issuedAt;
    private LocalDateTime executeBy;

    private CommandStatus status;

    private SecurityLevel securityLevel;
    private boolean emergency;

    /** ✅ ADD THIS */
    private String failureReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
