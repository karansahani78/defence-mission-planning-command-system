package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.CommandStatus;
import com.karan.mission_planning_system.enums.CommandType;
import com.karan.mission_planning_system.enums.SecurityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "commands",
        indexes = {
                @Index(name = "idx_command_code", columnList = "commandCode"),
                @Index(name = "idx_command_status", columnList = "status"),
                @Index(name = "idx_command_type", columnList = "commandType"),
                @Index(name = "idx_command_mission", columnList = "mission_id"),
                @Index(name = "idx_command_asset", columnList = "target_asset_id"),
                @Index(name = "idx_command_issued_at", columnList = "issuedAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String commandCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CommandType commandType;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String instruction;

    /* ================= CONTEXT ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_asset_id")
    private Asset targetAsset;

    /* ================= AUTHORITY ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by", nullable = false)
    private User issuedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_by")
    private User executedBy;

    /* ================= TIMING ================= */

    @NotNull
    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime executeBy;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime executedAt;

    /* ================= STATUS ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CommandStatus status;

    @Column(length = 500)
    private String failureReason;

    /* ================= SECURITY ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;

    @Column(nullable = false)
    private boolean emergency = false;

    /* ================= SYSTEM ================= */

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
