package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(source = "mission.id", target = "missionId")
    @Mapping(source = "mission.missionCode", target = "missionCode")
    AssetResponseDto assetToAssetResponseDto(Asset asset);

    Asset assetRequestDtoToAsset(AssetRequestDto assetRequestDto);
}

