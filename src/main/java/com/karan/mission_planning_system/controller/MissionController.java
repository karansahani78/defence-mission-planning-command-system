package com.karan.mission_planning_system.controller;

import com.karan.mission_planning_system.dto.MissionRequestDto;
import com.karan.mission_planning_system.dto.MissionResponseDto;
import com.karan.mission_planning_system.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    /* ================= CREATE ================= */

    /**
     * Mission planning is performed by Mission Planners only.
     */
    @PreAuthorize("hasRole('MISSION_PLANNER')")
    @PostMapping
    public ResponseEntity<MissionResponseDto> createMission(
            @Valid @RequestBody MissionRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(missionService.createMission(requestDto));
    }

    /* ================= UPDATE ================= */

    /**
     * Only planners can modify missions
     * while they are in DRAFT or PLANNED state.
     */
    @PreAuthorize("hasRole('MISSION_PLANNER')")
    @PutMapping("/{missionId}")
    public ResponseEntity<MissionResponseDto> updateMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(missionService.updateMission(missionId, requestDto));
    }

    /* ================= READ ================= */

    /**
     * All operational roles may view missions.
     */
    @PreAuthorize("""
        hasAnyRole(
            'SYSTEM_ADMIN',
            'MISSION_PLANNER',
            'MISSION_COMMANDER',
            'OPERATIONS_ANALYST',
            'SYSTEM'
        )
    """)
    @GetMapping("/{missionId}")
    public ResponseEntity<MissionResponseDto> getMissionById(
            @PathVariable Long missionId) {

        return ResponseEntity.ok(
                missionService.getMissionById(missionId)
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
    @GetMapping("/code/{missionCode}")
    public ResponseEntity<MissionResponseDto> getMissionByCode(
            @PathVariable String missionCode) {

        return ResponseEntity.ok(
                missionService.getMissionByCode(missionCode)
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
    @GetMapping
    public ResponseEntity<List<MissionResponseDto>> getAllMissions() {

        return ResponseEntity.ok(
                missionService.getAllMissions()
        );
    }

    /* ================= COMMAND AUTHORITY ================= */

    /**
     * ✔ SYSTEM_ADMIN approves mission
     * ✔ Assigns Mission Commander
     */
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping("/{missionId}/approve")
    public ResponseEntity<Void> approveMission(
            @PathVariable Long missionId,
            @RequestParam Long commanderId) {

        missionService.approveMission(missionId, commanderId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * ✔ Only assigned commander may start mission
     */
    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{missionId}/start")
    public ResponseEntity<Void> startMission(@PathVariable Long missionId) {

        missionService.startMission(missionId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * ✔ Hold mission (command decision)
     */
    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{missionId}/hold")
    public ResponseEntity<Void> holdMission(@PathVariable Long missionId) {

        missionService.holdMission(missionId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * ✔ Complete mission (assigned commander only)
     */
    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{missionId}/complete")
    public ResponseEntity<Void> completeMission(@PathVariable Long missionId) {

        missionService.completeMission(missionId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * ✔ Abort mission (assigned commander only)
     */
    @PreAuthorize("hasRole('MISSION_COMMANDER')")
    @PutMapping("/{missionId}/abort")
    public ResponseEntity<Void> abortMission(
            @PathVariable Long missionId,
            @RequestParam String reason) {

        missionService.abortMission(missionId, reason);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
