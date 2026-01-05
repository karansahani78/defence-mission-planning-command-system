package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.enums.AssetStatus;
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
        asset.setStatus(assetRequestDto.getStatus());
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
        Asset asset = assetRepository.findByAssetByCode(assetCode).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetCode));
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
        Mission mission = missionRepository.findById(missionId).orElseThrow(()-> new MissionNotFoundException("Mission not found"+missionId));
        asset.setMission(mission);
        asset.setStatus(AssetStatus.ASSIGNED);

    }

    @Override
    public void unassignAssetFromMission(Long assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        asset.setMission(null);
        asset.setStatus(AssetStatus.AVAILABLE);

    }

    @Override
    public void deactivateAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(()-> new AssetNotFoundException("Asset not found"+assetId));
        asset.setActive(false);
        asset.setStatus(AssetStatus.DECOMMISSIONED);

    }
}
