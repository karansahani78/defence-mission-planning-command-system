package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.MissionRequestDto;
import com.karan.mission_planning_system.dto.MissionResponseDto;
import com.karan.mission_planning_system.entity.Mission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MissionMapper {
    // Entity → DTO
    MissionResponseDto missionToMissionResponseDto(Mission mission);

    // DTO → Entity
    Mission missionRequestDtoToMission(MissionRequestDto missionRequestDto);
}
