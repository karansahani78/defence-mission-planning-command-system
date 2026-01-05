package com.karan.mission_planning_system.controller;

import com.karan.mission_planning_system.dto.AssetRequestDto;
import com.karan.mission_planning_system.dto.AssetResponseDto;
import com.karan.mission_planning_system.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto assetRequestDto) {
        AssetResponseDto response = assetService.createAsset(assetRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getAllAssets() {
        List<AssetResponseDto> response = assetService.getAllAssets();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{assetCode}")
    public ResponseEntity<AssetResponseDto> getAssetByCode(@PathVariable("assetCode") String assetCode) {
        AssetResponseDto response = assetService.getAssetByCode(assetCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable("assetId") Long assetId) {
        AssetResponseDto response = assetService.getAssetById(assetId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{assetId}/assign/{missionId}")
    public ResponseEntity<Void> assignAssetToMission(
            @PathVariable Long assetId,
            @PathVariable Long missionId) {

        assetService.assignAssetToMission(assetId, missionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{assetId}/unassign")
    public ResponseEntity<Void> unassignAssetFromMission(
            @PathVariable Long assetId) {

        assetService.unassignAssetFromMission(assetId);
        return ResponseEntity.noContent().build();
    }
}