package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.dto.MissionAssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.exception.AssetNotFoundException;
import com.karan.mission_planning_system.exception.MissionNotFoundException;
import com.karan.mission_planning_system.mapper.AssetMapper;
import com.karan.mission_planning_system.repository.AssetRepository;
import com.karan.mission_planning_system.repository.MissionRepository;
import com.karan.mission_planning_system.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final MissionRepository missionRepository;


    @Override
    public AssetResponseDto createAsset(AssetRequestDto assetRequestDto) {
       Asset asset = assetMapper.assetRequestDtoToAsset(assetRequestDto);
       asset.setActive(true);
      asset.setStatus(AssetStatus.AVAILABLE);

       Asset savedAsset = assetRepository.save(asset);
        return assetMapper.assetToAssetResponseDto(savedAsset);
    }

    @Override
    public AssetResponseDto updateAsset(Long assetId, AssetRequestDto assetRequestDto) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        asset.setAssetName(assetRequestDto.getAssetName());
        asset.setAssetType(assetRequestDto.getAssetType());
        asset.setDescription(assetRequestDto.getDescription());
        asset.setSecurityLevel(assetRequestDto.getSecurityLevel());
        asset.setCurrentLocation(assetRequestDto.getCurrentLocation());
        Asset savedAsset = assetRepository.save(asset);
        return assetMapper.assetToAssetResponseDto(savedAsset);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetById(Long assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        return assetMapper.assetToAssetResponseDto(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetByCode(String assetCode) {
        Asset asset = assetRepository.findByAssetCode(assetCode).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetCode));
        return assetMapper.assetToAssetResponseDto(asset);
    }

    @Override
    public List<AssetResponseDto> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::assetToAssetResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void assignAssetToMission(Long assetId, Long missionId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        if (!asset.isActive() || asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new IllegalStateException("Asset is not available for assignment");
        }
        Mission mission = missionRepository.findById(missionId).orElseThrow(()-> new MissionNotFoundException("Mission not found"+missionId));
        if (mission.getStatus() != MissionStatus.DRAFT &&
                mission.getStatus() != MissionStatus.PLANNED) {
            throw new IllegalStateException(
                    "Assets can be assigned only during mission planning phase");
        }
        asset.setMission(mission);
        asset.setStatus(AssetStatus.ASSIGNED);
        asset.setMission(mission);

    }

    @Override
    public void unassignAssetFromMission(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new AssetNotFoundException("Asset not found " + assetId));

        if (asset.getMission() == null) {
            throw new IllegalStateException("Asset is not assigned to any mission");
        }

        Mission mission = asset.getMission();

        if (mission.getStatus() == MissionStatus.IN_PROGRESS ||
                mission.getStatus() == MissionStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Cannot unassign asset from an active or completed mission");
        }

        asset.setMission(null);
        asset.setStatus(AssetStatus.AVAILABLE);
    }

    @Override
    public void deactivateAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        asset.setActive(false);
        asset.setStatus(AssetStatus.DECOMMISSIONED);

    }

    @Override
    public List<MissionAssetResponseDto> getAssetsByMission(Long missionId) {
        return assetRepository.findAll().stream().map(asset->MissionAssetResponseDto.builder()
                .id(asset.getId())
                .assetCode(asset.getAssetCode())
                .assetName(asset.getAssetName())
                .assetType(asset.getAssetType())
                .status(asset.getStatus())
                .build()).collect(Collectors.toList());
    }
}
