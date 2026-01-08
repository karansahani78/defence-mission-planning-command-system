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

    /* ================= IDENTITY ================= */

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

    /* ================= CLASSIFICATION ================= */

    @NotNull(message = "Security level is required")
    private SecurityLevel securityLevel;

    /* ================= OWNERSHIP & LOCATION ================= */

    @Size(max = 150)
    private String owningUnit;

    @Size(max = 200)
    private String homeBase;

    @Size(max = 200)
    private String currentLocation;

    /* ================= OPERATIONAL CAPABILITY ================= */

    @PositiveOrZero
    private Double maxSustainmentCapacity;

    @PositiveOrZero
    private Double currentSustainmentLevel;

    @PositiveOrZero
    private Double maxEnduranceHours;

    @PositiveOrZero
    private Double operationalRangeKm;

    /* ================= READINESS & HEALTH ================= */

    @Min(0)
    @Max(100)
    private Integer readinessLevel; // optional, defaults to 100

    @Size(max = 300)
    private String operationalRestriction;

    /* ================= MAINTENANCE ================= */

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceDueAt;

    /* ================= MISSION LINK ================= */

    /**
     * Optional mission assignment (validated in service layer)
     */
    private Long missionId;
}
