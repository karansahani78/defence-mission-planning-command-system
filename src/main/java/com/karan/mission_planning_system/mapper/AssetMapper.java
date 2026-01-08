package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    /* ENTITY → RESPONSE */

    @Mapping(source = "mission.id", target = "missionId")
    @Mapping(source = "mission.missionCode", target = "missionCode")
    AssetResponseDto assetToAssetResponseDto(Asset asset);

    /* REQUEST → ENTITY */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mission", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Asset assetRequestDtoToAsset(AssetRequestDto assetRequestDto);
}
