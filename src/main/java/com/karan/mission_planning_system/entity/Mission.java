package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.MissionPriority;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
                @Index(name = "idx_mission_priority", columnList = "priority"),
                @Index(name = "idx_mission_security", columnList = "securityLevel"),
                @Index(name = "idx_mission_start_time", columnList = "startTime"),
                @Index(name = "idx_mission_created_at", columnList = "createdAt"),
                @Index(name = "idx_mission_approved_at", columnList = "approvedAt")
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MissionStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MissionPriority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;

    @Positive
    private Double estimatedDurationHours;

    @Positive
    private Double estimatedRangeKm;

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer minAssetReadiness = 70;

    @PositiveOrZero
    private Double minRequiredSustainmentLevel;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    @Size(max = 200)
    private String operationArea;

    @Size(max = 500)
    private String abortReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commander_id")
    private User commander;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "started_by")
    private User startedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aborted_by")
    private User abortedBy;

    private LocalDateTime abortedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
