package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.dto.MissionAssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.enums.OperationalState;
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
    public AssetResponseDto createAsset(AssetRequestDto dto) {

        Asset asset = assetMapper.assetRequestDtoToAsset(dto);

        asset.setActive(true);
        asset.setStatus(AssetStatus.AVAILABLE);

        asset.setReadinessLevel(
                dto.getReadinessLevel() != null ? dto.getReadinessLevel() : 100
        );

        asset.setMinimumReadinessRequired(70);
        asset.setOperationalState(OperationalState.OPERATIONAL);
        asset.setRequiresCommanderApproval(false);

        Asset saved = assetRepository.save(asset);
        return assetMapper.assetToAssetResponseDto(saved);
    }

    @Override
    public AssetResponseDto updateAsset(Long assetId, AssetRequestDto dto) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));

        asset.setAssetName(dto.getAssetName());
        asset.setAssetType(dto.getAssetType());
        asset.setDescription(dto.getDescription());
        asset.setSecurityLevel(dto.getSecurityLevel());

        asset.setOwningUnit(dto.getOwningUnit());
        asset.setHomeBase(dto.getHomeBase());
        asset.setCurrentLocation(dto.getCurrentLocation());

        asset.setMaxSustainmentCapacity(dto.getMaxSustainmentCapacity());
        asset.setCurrentSustainmentLevel(dto.getCurrentSustainmentLevel());
        asset.setMaxEnduranceHours(dto.getMaxEnduranceHours());
        asset.setOperationalRangeKm(dto.getOperationalRangeKm());

        if (dto.getReadinessLevel() != null) {
            asset.setReadinessLevel(dto.getReadinessLevel());
        }

        asset.setOperationalRestriction(dto.getOperationalRestriction());

        asset.setLastMaintenanceAt(dto.getLastMaintenanceAt());
        asset.setNextMaintenanceDueAt(dto.getNextMaintenanceDueAt());

        Asset saved = assetRepository.save(asset);
        return assetMapper.assetToAssetResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetById(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));
        return assetMapper.assetToAssetResponseDto(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetByCode(String assetCode) {
        Asset asset = assetRepository.findByAssetCode(assetCode)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetCode));
        return assetMapper.assetToAssetResponseDto(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponseDto> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::assetToAssetResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void assignAssetToMission(Long assetId, Long missionId) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));

        if (!asset.isActive()
                || asset.getStatus() != AssetStatus.AVAILABLE
                || asset.getReadinessLevel() < 70) {

            throw new IllegalStateException(
                    "Asset is not operationally ready for assignment");
        }

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException("Mission not found: " + missionId));

        if (mission.getStatus() != MissionStatus.DRAFT &&
                mission.getStatus() != MissionStatus.PLANNED) {

            throw new IllegalStateException(
                    "Assets can only be assigned during mission planning");
        }

        asset.setMission(mission);
        asset.setStatus(AssetStatus.ASSIGNED);
    }

    @Override
    public void unassignAssetFromMission(Long assetId) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));

        Mission mission = asset.getMission();
        if (mission == null) {
            throw new IllegalStateException("Asset is not assigned to any mission");
        }

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

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));

        asset.setActive(false);
        asset.setStatus(AssetStatus.DECOMMISSIONED);
        asset.setReadinessLevel(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionAssetResponseDto> getAssetsByMission(Long missionId) {

        return assetRepository.findByMissionId(missionId).stream()
                .map(asset -> MissionAssetResponseDto.builder()
                        .id(asset.getId())
                        .assetCode(asset.getAssetCode())
                        .assetName(asset.getAssetName())
                        .assetType(asset.getAssetType())
                        .status(asset.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
