package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.AssetStatus;
import com.karan.mission_planning_system.enums.AssetType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "assets",
        indexes = {
                @Index(name = "idx_asset_code", columnList = "assetCode"),
                @Index(name = "idx_asset_status", columnList = "status"),
                @Index(name = "idx_asset_type", columnList = "assetType")
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
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String assetName;

    @Size(max = 500)
    @Column(length = 500)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssetStatus status;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;


    @Size(max = 200)
    private String currentLocation;


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
