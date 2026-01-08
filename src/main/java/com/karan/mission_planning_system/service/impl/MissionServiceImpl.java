package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.MissionRequestDto;
import com.karan.mission_planning_system.dto.MissionResponseDto;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.entity.User;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.enums.Role;
import com.karan.mission_planning_system.exception.MissionNotFoundException;
import com.karan.mission_planning_system.mapper.MissionMapper;
import com.karan.mission_planning_system.repository.MissionRepository;
import com.karan.mission_planning_system.repository.UserRepository;
import com.karan.mission_planning_system.security.SecurityUtil;
import com.karan.mission_planning_system.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MissionMapper missionMapper;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    @Override
    public MissionResponseDto createMission(MissionRequestDto dto) {

        validateTimeline(dto.getStartTime(), dto.getPlannedEndTime());

        User creator = securityUtil.getCurrentUser();

        Mission mission = missionMapper.missionRequestDtoToMission(dto);

        mission.setStatus(MissionStatus.DRAFT);
        mission.setCreatedBy(creator);

        mission.setMinAssetReadiness(
                dto.getMinAssetReadiness() != null ? dto.getMinAssetReadiness() : 70
        );
        mission.setMinRequiredSustainmentLevel(dto.getMinRequiredSustainmentLevel());

        return missionMapper.missionToMissionResponseDto(
                missionRepository.save(mission)
        );
    }

    @Override
    public MissionResponseDto updateMission(Long missionId, MissionRequestDto dto) {

        Mission mission = getMission(missionId);

        if (mission.getStatus() != MissionStatus.DRAFT &&
                mission.getStatus() != MissionStatus.PLANNED) {
            throw new IllegalStateException(
                    "Only DRAFT or PLANNED missions can be modified");
        }

        validateTimeline(dto.getStartTime(), dto.getPlannedEndTime());

        mission.setMissionName(dto.getMissionName());
        mission.setMissionObjective(dto.getMissionObjective());
        mission.setPriority(dto.getPriority());
        mission.setSecurityLevel(dto.getSecurityLevel());
        mission.setEstimatedDurationHours(dto.getEstimatedDurationHours());
        mission.setEstimatedRangeKm(dto.getEstimatedRangeKm());
        mission.setStartTime(dto.getStartTime());
        mission.setPlannedEndTime(dto.getPlannedEndTime());
        mission.setOperationArea(dto.getOperationArea());

        if (dto.getMinAssetReadiness() != null) {
            mission.setMinAssetReadiness(dto.getMinAssetReadiness());
        }

        if (dto.getMinRequiredSustainmentLevel() != null) {
            mission.setMinRequiredSustainmentLevel(dto.getMinRequiredSustainmentLevel());
        }

        return missionMapper.missionToMissionResponseDto(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionResponseDto getMissionById(Long missionId) {
        return missionMapper.missionToMissionResponseDto(getMission(missionId));
    }

    @Override
    @Transactional(readOnly = true)
    public MissionResponseDto getMissionByCode(String missionCode) {
        return missionMapper.missionToMissionResponseDto(
                missionRepository.findByMissionCode(missionCode)
                        .orElseThrow(() ->
                                new MissionNotFoundException(
                                        "Mission not found: " + missionCode))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getAllMissions() {
        return missionRepository.findAll()
                .stream()
                .map(missionMapper::missionToMissionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void approveMission(Long missionId, Long commanderId) {

        securityUtil.requireRole("ROLE_SYSTEM_ADMIN");

        Mission mission = getMission(missionId);

        if (mission.getStatus() != MissionStatus.DRAFT &&
                mission.getStatus() != MissionStatus.PLANNED) {
            throw new IllegalStateException(
                    "Mission cannot be approved in current state");
        }

        User commander = getUser(commanderId);

        if (commander.getRole() != Role.ROLE_MISSION_COMMANDER) {
            throw new IllegalArgumentException(
                    "Assigned user is not a Mission Commander");
        }

        mission.setCommander(commander);
        mission.setApprovedBy(securityUtil.getCurrentUser());
        mission.setApprovedAt(LocalDateTime.now());
        mission.setStatus(MissionStatus.APPROVED);
    }

    @Override
    public void startMission(Long missionId) {

        Mission mission = getMission(missionId);
        User currentUser = securityUtil.getCurrentUser();

        if (mission.getStatus() != MissionStatus.APPROVED) {
            throw new IllegalStateException(
                    "Mission must be APPROVED before start");
        }

        if (!mission.getCommander().getId().equals(currentUser.getId())) {
            throw new SecurityException(
                    "Only assigned commander can start this mission");
        }

        mission.setStatus(MissionStatus.IN_PROGRESS);
        mission.setStartedBy(currentUser);
        mission.setActualStartTime(LocalDateTime.now());
    }

    @Override
    public void holdMission(Long missionId) {

        Mission mission = getMission(missionId);

        if (mission.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only IN_PROGRESS missions can be held");
        }

        mission.setStatus(MissionStatus.ON_HOLD);
    }

    @Override
    public void completeMission(Long missionId) {

        Mission mission = getMission(missionId);
        User currentUser = securityUtil.getCurrentUser();

        if (mission.getStatus() != MissionStatus.IN_PROGRESS &&
                mission.getStatus() != MissionStatus.ON_HOLD) {
            throw new IllegalStateException("Mission not active");
        }

        if (!mission.getCommander().getId().equals(currentUser.getId())) {
            throw new SecurityException(
                    "Only assigned commander can complete mission");
        }

        mission.setStatus(MissionStatus.COMPLETED);
        mission.setCompletedBy(currentUser);
        mission.setActualEndTime(LocalDateTime.now());
    }

    @Override
    public void abortMission(Long missionId, String reason) {

        Mission mission = getMission(missionId);
        User currentUser = securityUtil.getCurrentUser();

        if (mission.getStatus() == MissionStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed mission cannot be aborted");
        }

        if (!mission.getCommander().getId().equals(currentUser.getId())) {
            throw new SecurityException(
                    "Only assigned commander can abort mission");
        }

        mission.setStatus(MissionStatus.ABORTED);
        mission.setAbortReason(reason);
        mission.setAbortedBy(currentUser);
        mission.setAbortedAt(LocalDateTime.now());
    }

    private Mission getMission(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new MissionNotFoundException(
                                "Mission not found with id: " + missionId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User not found: " + userId));
    }

    private void validateTimeline(LocalDateTime start, LocalDateTime plannedEnd) {
        if (plannedEnd != null && plannedEnd.isBefore(start)) {
            throw new IllegalArgumentException(
                    "Planned end time cannot be before start time");
        }
    }
}
