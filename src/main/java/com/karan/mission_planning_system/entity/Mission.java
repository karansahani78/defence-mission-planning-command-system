package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.MissionPriority;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "missions",
        indexes = {
                @Index(name = "idx_mission_code", columnList = "missionCode"),
                @Index(name = "idx_mission_status", columnList = "status"),
                @Index(name = "idx_mission_start_time", columnList = "startTime")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique mission identifier (e.g. OPS-NEP-2026-001)
     */
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String missionCode;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String missionName;

    @Size(max = 1000)
    @Column(length = 1000)
    private String missionObjective;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MissionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;

    /**
     * Mission timeline
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * Geographical area (can be lat/long or region name)
     */
    @Size(max = 200)
    private String operationArea;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commander_id")
    private User commander;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
