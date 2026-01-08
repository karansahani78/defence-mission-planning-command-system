package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.MissionRequestDto;
import com.karan.mission_planning_system.dto.MissionResponseDto;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper(componentModel = "spring")
public interface MissionMapper {

    @Mappings({
            @Mapping(source = "createdBy", target = "createdBy"),
            @Mapping(source = "commander", target = "commander"),
            @Mapping(source = "approvedBy", target = "approvedBy"),
            @Mapping(source = "startedBy", target = "startedBy"),
            @Mapping(source = "completedBy", target = "completedBy"),
            @Mapping(source = "abortedBy", target = "abortedBy")
    })
    MissionResponseDto missionToMissionResponseDto(Mission mission);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),

            @Mapping(target = "actualStartTime", ignore = true),
            @Mapping(target = "actualEndTime", ignore = true),
            @Mapping(target = "abortReason", ignore = true),

            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "commander", ignore = true),
            @Mapping(target = "approvedBy", ignore = true),
            @Mapping(target = "approvedAt", ignore = true),
            @Mapping(target = "startedBy", ignore = true),
            @Mapping(target = "completedBy", ignore = true),
            @Mapping(target = "abortedBy", ignore = true),
            @Mapping(target = "abortedAt", ignore = true),

            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Mission missionRequestDtoToMission(MissionRequestDto dto);

    default String map(User user) {
        return user != null ? user.getUsername() : null;
    }
}
