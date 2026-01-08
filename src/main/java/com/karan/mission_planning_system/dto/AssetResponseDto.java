package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.AssetType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDto {

    private Long id;
    private String assetCode;
    private AssetType assetType;
    private String assetName;
    private String description;

    private AssetStatus status;
    private SecurityLevel securityLevel;

    private String owningUnit;
    private String homeBase;
    private String currentLocation;

    private Double maxSustainmentCapacity;
    private Double currentSustainmentLevel;
    private Double maxEnduranceHours;
    private Double operationalRangeKm;

    private Integer readinessLevel;
    private String operationalRestriction;

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceDueAt;

    private Long missionId;
    private String missionCode;

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
