package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;
import com.karan.mission_planning_system.entity.Command;
import com.karan.mission_planning_system.entity.Asset;
import com.karan.mission_planning_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper(componentModel = "spring")
public interface CommandMapper {

    @Mappings({
            @Mapping(source = "mission.id", target = "missionId"),
            @Mapping(source = "mission.missionCode", target = "missionCode"),

            @Mapping(source = "targetAsset.id", target = "targetAssetId"),
            @Mapping(source = "targetAsset.assetCode", target = "targetAssetCode"),

            @Mapping(source = "issuedBy", target = "issuedBy"),
            @Mapping(source = "assignedTo", target = "assignedTo"),
            @Mapping(source = "executedBy", target = "executedBy")
    })
    CommandResponseDto commandToCommandResponseDto(Command command);

    /* helpers */
    default String map(User user) {
        return user != null ? user.getUsername() : null;
    }

    default Long map(Asset asset) {
        return asset != null ? asset.getId() : null;
    }
}
