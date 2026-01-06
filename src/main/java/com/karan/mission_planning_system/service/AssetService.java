package com.karan.mission_planning_system.service;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.dto.MissionAssetResponseDto;

import java.util.List;

public interface AssetService {
    //createAsset
    AssetResponseDto createAsset(AssetRequestDto assetRequestDto);
    //updateAsset
    AssetResponseDto updateAsset(Long assetId,AssetRequestDto assetRequestDto);
    //getAssetById
    AssetResponseDto getAssetById(Long assetId);
    // getAssetByCode
    AssetResponseDto getAssetByCode(String assetCode);
    //getAllAssets
    List<AssetResponseDto> getAllAssets();
    // void assignAssetToMission
    void assignAssetToMission(Long assetId,Long missionId);
    // void unassignAssetFromMission
    void unassignAssetFromMission(Long assetId);
    // void deactivateAsset
    void deactivateAsset(Long assetId);
    List<MissionAssetResponseDto> getAssetsByMission(Long missionId);
}
