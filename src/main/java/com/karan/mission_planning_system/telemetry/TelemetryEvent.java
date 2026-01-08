package com.karan.mission_planning_system.telemetry;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryEvent {

    private String eventId;
    private LocalDateTime eventTime;

    private String eventType;       // COMMAND_ISSUED, COMMAND_ACKNOWLEDGED, etc.
    private String sourceService;
    private String nodeId;

    private Long commandId;
    private String commandCode;
    private String commandStatus;

    private Long missionId;
    private String missionCode;

    private String actor;           // username
    private String severity;        // INFO / WARNING / CRITICAL
}
