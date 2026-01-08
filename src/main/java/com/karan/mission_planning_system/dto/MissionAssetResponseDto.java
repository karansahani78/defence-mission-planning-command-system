package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.AssetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionAssetResponseDto {

    private Long id;
    private String assetCode;
    private String assetName;
    private AssetType assetType;
    private AssetStatus status;
}
