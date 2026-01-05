package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.UserRequestDto;
import com.karan.mission_planning_system.dto.UserResponseDto;
import com.karan.mission_planning_system.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Entity → DTO
    UserResponseDto userToUserResponseDto(User user);

    // DTO → Entity
    User  userResponseDtoToUser(UserRequestDto userRequestDto);
}
