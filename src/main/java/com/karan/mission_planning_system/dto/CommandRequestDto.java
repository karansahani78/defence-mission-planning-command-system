package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.CommandType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandRequestDto {

    @NotBlank
    private String commandCode;

    @NotNull
    private CommandType commandType;

    @NotBlank
    private String instruction;

    @NotNull
    private Long missionId;

    private Long targetAssetId;
    private Long assignedToUserId;

    private LocalDateTime executeBy;

    @NotNull
    private SecurityLevel securityLevel;

    private boolean emergency;
}
