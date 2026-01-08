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

    /* ================= IDENTITY ================= */

    private Long id;
    private String assetCode;
    private AssetType assetType;
    private String assetName;
    private String description;

    /* ================= CLASSIFICATION ================= */

    private AssetStatus status;
    private SecurityLevel securityLevel;

    /* ================= OWNERSHIP & LOCATION ================= */

    private String owningUnit;
    private String homeBase;
    private String currentLocation;

    /* ================= OPERATIONAL CAPABILITY ================= */

    private Double maxSustainmentCapacity;
    private Double currentSustainmentLevel;
    private Double maxEnduranceHours;
    private Double operationalRangeKm;

    /* ================= READINESS & HEALTH ================= */

    private Integer readinessLevel;
    private String operationalRestriction;

    /* ================= MAINTENANCE ================= */

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceDueAt;

    /* ================= MISSION (CONTROLLED EXPOSURE) ================= */

    private Long missionId;
    private String missionCode;

    /* ================= SYSTEM ================= */

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
