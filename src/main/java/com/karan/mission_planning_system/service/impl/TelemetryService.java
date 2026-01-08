package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.entity.Command;
import com.karan.mission_planning_system.kafka.TelemetryProducer;
import com.karan.mission_planning_system.telemetry.TelemetryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryProducer producer;

    @Value("${telemetry.service.name}")
    private String serviceName;

    @Value("${telemetry.node-id}")
    private String nodeId;

    public void publishCommandEvent(Command command, String eventType) {

        TelemetryEvent event = TelemetryEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventTime(LocalDateTime.now())
                .eventType(eventType)
                .sourceService(serviceName)
                .nodeId(nodeId)

                .commandId(command.getId())
                .commandCode(command.getCommandCode())
                .commandStatus(command.getStatus().name())

                .missionId(command.getMission().getId())
                .missionCode(command.getMission().getMissionCode())

                .actor(command.getIssuedBy() != null
                        ? command.getIssuedBy().getUsername()
                        : "SYSTEM")

                .severity(command.isEmergency() ? "CRITICAL" : "INFO")
                .build();

        producer.publish(event);
    }
}
