package com.karan.mission_planning_system.entity;

import com.karan.mission_planning_system.enums.CommandStatus;
import com.karan.mission_planning_system.enums.CommandType;
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
        name = "commands",
        indexes = {
                @Index(name = "idx_command_code", columnList = "commandCode"),
                @Index(name = "idx_command_status", columnList = "status"),
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
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String commandCode;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CommandType commandType;


    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String instruction;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by", nullable = false)
    private User issuedBy;

    /**
     * Who must execute the command (unit / operator / system)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;


    @NotNull
    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime executeBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CommandStatus status;

    /**
     * Security classification
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SecurityLevel securityLevel;

    /**
     * Emergency / override command flag
     */
    @Column(nullable = false)
    private boolean emergency;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
