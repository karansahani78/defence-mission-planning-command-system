package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "assets",
        indexes = {
                @Index(name = "idx_asset_code", columnList = "assetCode"),
                @Index(name = "idx_asset_status", columnList = "status"),
                @Index(name = "idx_asset_operational_state", columnList = "operationalState"),
                @Index(name = "idx_asset_type", columnList = "assetType"),
                @Index(name = "idx_asset_readiness", columnList = "readinessLevel"),
                @Index(name = "idx_asset_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String assetCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetType assetType;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String assetName;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OperationalState operationalState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;

    @Size(max = 150)
    @Column(length = 150)
    private String owningUnit;

    @Size(max = 200)
    @Column(length = 200)
    private String homeBase;

    @Size(max = 200)
    @Column(length = 200)
    private String currentLocation;

    @Positive
    private Double maxSustainmentCapacity;

    @PositiveOrZero
    private Double currentSustainmentLevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private SustainmentUnit sustainmentUnit;

    @Positive
    private Double maxEnduranceHours;

    @Positive
    private Double operationalRangeKm;

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer readinessLevel = 100;

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer minimumReadinessRequired = 70;

    @PositiveOrZero
    private Double minimumSustainmentRequired;

    @Size(max = 300)
    @Column(length = 300)
    private String operationalRestriction;

    @Column(nullable = false)
    private boolean requiresCommanderApproval = false;

    @Size(max = 100)
    @Column(length = 100)
    private String approvalAuthority;

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceDueAt;

    private LocalDate commissionedDate;
    private LocalDate expectedServiceEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
