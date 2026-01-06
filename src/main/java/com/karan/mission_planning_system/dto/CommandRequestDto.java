package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.CommandType;
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
public class CommandRequestDto {

    @NotBlank
    @Size(max = 50)
    private String commandCode;

    @NotNull
    private CommandType commandType;

    @NotBlank
    @Size(max = 2000)
    private String instruction;

    @NotNull
    private Long missionId;

    /**
     * Optional – specific user/unit to execute
     */
    private Long assignedToUserId;

    private LocalDateTime executeBy;

    @NotNull
    private SecurityLevel securityLevel;

    private boolean emergency;
}
