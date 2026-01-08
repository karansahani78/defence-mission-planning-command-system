package com.karan.mission_planning_system.controller;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;
import com.karan.mission_planning_system.service.CommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PostMapping
    public ResponseEntity<CommandResponseDto> issueCommand(
            @Valid @RequestBody CommandRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandService.issueCommand(requestDto));
    }

    @PreAuthorize("""
        hasAnyRole(
            'SYSTEM_ADMIN',
            'MISSION_PLANNER',
            'MISSION_COMMANDER',
            'OPERATIONS_ANALYST',
            'SYSTEM'
        )
    """)
    @GetMapping("/{commandId}")
    public ResponseEntity<CommandResponseDto> getCommandById(
            @PathVariable Long commandId) {

        return ResponseEntity.ok(
                commandService.getCommandById(commandId)
        );
    }

    @PreAuthorize("""
        hasAnyRole(
            'SYSTEM_ADMIN',
            'MISSION_PLANNER',
            'MISSION_COMMANDER',
            'OPERATIONS_ANALYST',
            'SYSTEM'
        )
    """)
    @GetMapping("/code/{commandCode}")
    public ResponseEntity<CommandResponseDto> getCommandByCode(
            @PathVariable String commandCode) {

        return ResponseEntity.ok(
                commandService.getCommandByCode(commandCode)
        );
    }

    @PreAuthorize("""
        hasAnyRole(
            'SYSTEM_ADMIN',
            'MISSION_PLANNER',
            'MISSION_COMMANDER',
            'OPERATIONS_ANALYST',
            'SYSTEM'
        )
    """)
    @GetMapping("/mission/{missionId}")
    public ResponseEntity<List<CommandResponseDto>> getCommandsByMission(
            @PathVariable Long missionId) {

        return ResponseEntity.ok(
                commandService.getCommandsByMission(missionId)
        );
    }

    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{commandId}/acknowledge")
    public ResponseEntity<Void> acknowledgeCommand(
            @PathVariable Long commandId) {

        commandService.acknowledgeCommand(commandId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{commandId}/execute")
    public ResponseEntity<Void> executeCommand(
            @PathVariable Long commandId) {

        commandService.executeCommand(commandId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{commandId}/complete")
    public ResponseEntity<Void> completeCommand(
            @PathVariable Long commandId) {

        commandService.completeCommand(commandId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{commandId}/fail")
    public ResponseEntity<Void> failCommand(
            @PathVariable Long commandId,
            @RequestParam String reason) {

        commandService.failCommand(commandId, reason);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','MISSION_COMMANDER')")
    @PutMapping("/{commandId}/cancel")
    public ResponseEntity<Void> cancelCommand(
            @PathVariable Long commandId) {

        commandService.cancelCommand(commandId);
        return ResponseEntity.noContent().build();
    }
}
