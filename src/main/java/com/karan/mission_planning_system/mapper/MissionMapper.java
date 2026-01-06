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

    /* ENTITY → DTO */

    @Mappings({
            @Mapping(source = "createdBy", target = "createdBy"),
            @Mapping(source = "commander", target = "commander"),
            @Mapping(source = "approvedBy", target = "approvedBy"),
            @Mapping(source = "startedBy", target = "startedBy"),
            @Mapping(source = "completedBy", target = "completedBy"),
            @Mapping(source = "abortedBy", target = "abortedBy")
    })
    MissionResponseDto missionToMissionResponseDto(Mission mission);

    /* DTO → ENTITY */

    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "status"),
            @Mapping(ignore = true, target = "actualStartTime"),
            @Mapping(ignore = true, target = "actualEndTime"),
            @Mapping(ignore = true, target = "abortReason"),

            @Mapping(ignore = true, target = "createdBy"),
            @Mapping(ignore = true, target = "commander"),
            @Mapping(ignore = true, target = "approvedBy"),
            @Mapping(ignore = true, target = "approvedAt"),
            @Mapping(ignore = true, target = "startedBy"),
            @Mapping(ignore = true, target = "completedBy"),
            @Mapping(ignore = true, target = "abortedBy"),
            @Mapping(ignore = true, target = "abortedAt"),

            @Mapping(ignore = true, target = "createdAt"),
            @Mapping(ignore = true, target = "updatedAt")
    })
    Mission missionRequestDtoToMission(MissionRequestDto dto);

    /*HELPERS */

    default String map(User user) {
        return user != null ? user.getUsername() : null;
    }
}
