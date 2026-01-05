package com.karan.mission_planning_system.dto;

import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.AssetType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDto {

    @NotBlank(message = "Asset code is required")
    @Size(max = 50)
    private String assetCode;   // e.g. DRONE-NEP-007

    @NotNull(message = "Asset type is required")
    private AssetType assetType;

    @NotBlank(message = "Asset name is required")
    @Size(max = 100)
    private String assetName;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Asset status is required")
    private AssetStatus status;

    @NotNull(message = "Security level is required")
    private SecurityLevel securityLevel;

    @Size(max = 200)
    private String currentLocation;

    /**
     * Optional: assign asset to mission
     */
    private Long missionId;
}
