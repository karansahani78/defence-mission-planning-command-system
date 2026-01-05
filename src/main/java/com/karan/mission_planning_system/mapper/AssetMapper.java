package com.karan.mission_planning_system.mapper;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    // Entity → DTO
    AssetResponseDto assetToAssetResponseDto(Asset asset);

    // DTO → Entity
    Asset assetRequestDtoToAsset(AssetRequestDto assetRequestDto);
}
