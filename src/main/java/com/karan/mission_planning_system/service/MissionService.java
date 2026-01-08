package com.karan.mission_planning_system.service;

import com.karan.mission_planning_system.dto.MissionRequestDto;
import com.karan.mission_planning_system.dto.MissionResponseDto;

import java.util.List;

public interface MissionService {

    // Create a new mission (DRAFT)
    MissionResponseDto createMission(MissionRequestDto requestDto);

    // Update mission details (only if DRAFT / PLANNED)
    MissionResponseDto updateMission(Long missionId, MissionRequestDto requestDto);

    // Get mission by database ID
    MissionResponseDto getMissionById(Long missionId);

    // Get mission by unique mission code
    MissionResponseDto getMissionByCode(String missionCode);

    // Fetch all missions
    List<MissionResponseDto> getAllMissions();

    // ✅ Approve mission AND assign commander (SYSTEM_ADMIN only)
    void approveMission(Long missionId, Long commanderId);

    // Start mission execution (COMMANDER)
    void startMission(Long missionId);

    // Put mission on hold (COMMANDER)
    void holdMission(Long missionId);

    // Complete mission successfully (COMMANDER)
    void completeMission(Long missionId);

    // Abort mission (COMMANDER)
    void abortMission(Long missionId, String reason);
}
