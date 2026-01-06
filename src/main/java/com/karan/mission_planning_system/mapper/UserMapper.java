package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.UserRequestDto;
import com.karan.mission_planning_system.dto.UserResponseDto;
import com.karan.mission_planning_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /*ENTITY → RESPONSE DTO */
    UserResponseDto userToUserResponseDto(User user);

    /* REQUEST DTO → ENTITY */
    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "enabled"),
            @Mapping(ignore = true, target = "accountNonLocked"),
            @Mapping(ignore = true, target = "passwordResetOtp"),
            @Mapping(ignore = true, target = "otpExpiryTime"),
            @Mapping(ignore = true, target = "createdAt")
    })
    User userRequestDtoToUser(UserRequestDto dto);
}
