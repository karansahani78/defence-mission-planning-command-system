package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.AssetType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDto {

    @NotBlank(message = "Asset code is required")
    @Size(max = 50)
    private String assetCode;

    @NotNull(message = "Asset type is required")
    private AssetType assetType;

    @NotBlank(message = "Asset name is required")
    @Size(max = 100)
    private String assetName;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Security level is required")
    private SecurityLevel securityLevel;

    @Size(max = 150)
    private String owningUnit;

    @Size(max = 200)
    private String homeBase;

    @Size(max = 200)
    private String currentLocation;

    @PositiveOrZero
    private Double maxSustainmentCapacity;

    @PositiveOrZero
    private Double currentSustainmentLevel;

    @PositiveOrZero
    private Double maxEnduranceHours;

    @PositiveOrZero
    private Double operationalRangeKm;

    @Min(0)
    @Max(100)
    private Integer readinessLevel;

    @Size(max = 300)
    private String operationalRestriction;

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceDueAt;

    private Long missionId;
}
