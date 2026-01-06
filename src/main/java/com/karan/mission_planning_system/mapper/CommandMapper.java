package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;
import com.karan.mission_planning_system.entity.Command;
import com.karan.mission_planning_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommandMapper {

    /* ========= ENTITY → DTO ========= */

    @Mappings({
            @Mapping(source = "mission.id", target = "missionId"),
            @Mapping(source = "mission.missionCode", target = "missionCode"),

            @Mapping(source = "issuedBy", target = "issuedBy"),
            @Mapping(source = "assignedTo", target = "assignedTo"),

            @Mapping(ignore = true, target = "failureReason") // if not in DTO
    })
    CommandResponseDto commandToCommandResponseDto(Command command);

    /* ========= DTO → ENTITY ========= */

    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "mission"),
            @Mapping(ignore = true, target = "issuedBy"),
            @Mapping(ignore = true, target = "assignedTo"),
            @Mapping(ignore = true, target = "issuedAt"),
            @Mapping(ignore = true, target = "status"),
            @Mapping(ignore = true, target = "createdAt"),
            @Mapping(ignore = true, target = "updatedAt")
    })
    Command commandRequestDtoToCommand(CommandRequestDto dto);

    /* ========= HELPERS ========= */

    default String map(User user) {
        return user != null ? user.getUsername() : null;
    }
}
