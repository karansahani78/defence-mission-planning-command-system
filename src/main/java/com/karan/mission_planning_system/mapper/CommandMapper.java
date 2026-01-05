package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;
import com.karan.mission_planning_system.entity.Command;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    // Entity → DTO
    CommandResponseDto commandToCommandResponseDto(Command command);

    // DTO → Entity
    Command commandRequestDtoToCommand(CommandRequestDto commandRequestDto);


}
