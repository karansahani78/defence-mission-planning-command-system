package com.karan.mission_planning_system.service.impl;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;
import com.karan.mission_planning_system.entity.Asset;
import com.karan.mission_planning_system.entity.Command;
import com.karan.mission_planning_system.entity.Mission;
import com.karan.mission_planning_system.entity.User;
import com.karan.mission_planning_system.enums.CommandStatus;
import com.karan.mission_planning_system.enums.MissionStatus;
import com.karan.mission_planning_system.exception.CommandNotFoundException;
import com.karan.mission_planning_system.exception.MissionNotFoundException;
import com.karan.mission_planning_system.mapper.CommandMapper;
import com.karan.mission_planning_system.repository.AssetRepository;
import com.karan.mission_planning_system.repository.CommandRepository;
import com.karan.mission_planning_system.repository.MissionRepository;
import com.karan.mission_planning_system.repository.UserRepository;
import com.karan.mission_planning_system.security.SecurityUtil;
import com.karan.mission_planning_system.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final MissionRepository missionRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final CommandMapper commandMapper;
    private final SecurityUtil securityUtil;
    private final TelemetryService telemetryService;

    /* ================= ISSUE ================= */

    @Override
    public CommandResponseDto issueCommand(CommandRequestDto requestDto) {

        securityUtil.requireRole("ROLE_MISSION_COMMANDER");

        if (commandRepository.existsByCommandCode(requestDto.getCommandCode())) {
            throw new IllegalStateException(
                    "Command code already exists: " + requestDto.getCommandCode());
        }

        Mission mission = missionRepository.findById(requestDto.getMissionId())
                .orElseThrow(() ->
                        new MissionNotFoundException(
                                "Mission not found: " + requestDto.getMissionId()));

        if (mission.getStatus() != MissionStatus.APPROVED &&
                mission.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Commands can only be issued for APPROVED or IN_PROGRESS missions");
        }

        User issuer = securityUtil.getCurrentUser();

        User assignedTo = null;
        if (requestDto.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(requestDto.getAssignedToUserId())
                    .orElseThrow(() ->
                            new IllegalStateException("Assigned user not found"));
        }

        Asset targetAsset = null;
        if (requestDto.getTargetAssetId() != null) {
            targetAsset = assetRepository.findById(requestDto.getTargetAssetId())
                    .orElseThrow(() ->
                            new IllegalStateException("Target asset not found"));
        }

        Command command = Command.builder()
                .commandCode(requestDto.getCommandCode())
                .commandType(requestDto.getCommandType())
                .instruction(requestDto.getInstruction())
                .mission(mission)
                .targetAsset(targetAsset)
                .issuedBy(issuer)
                .assignedTo(assignedTo)
                .issuedAt(LocalDateTime.now())
                .executeBy(requestDto.getExecuteBy())
                .status(CommandStatus.ISSUED)
                .securityLevel(requestDto.getSecurityLevel())
                .emergency(requestDto.isEmergency())
                .build();

        Command saved = commandRepository.save(command);

        safeTelemetry(saved, "COMMAND_ISSUED");

        return commandMapper.commandToCommandResponseDto(saved);
    }

    /* ================= READ ================= */

    @Override
    @Transactional(readOnly = true)
    public CommandResponseDto getCommandById(Long commandId) {
        return commandMapper.commandToCommandResponseDto(getCommand(commandId));
    }

    @Override
    @Transactional(readOnly = true)
    public CommandResponseDto getCommandByCode(String commandCode) {
        return commandRepository.findByCommandCode(commandCode)
                .map(commandMapper::commandToCommandResponseDto)
                .orElseThrow(() ->
                        new CommandNotFoundException(
                                "Command not found: " + commandCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandResponseDto> getCommandsByMission(Long missionId) {
        return commandRepository.findAllByMissionId(missionId)
                .stream()
                .map(commandMapper::commandToCommandResponseDto)
                .collect(Collectors.toList());
    }

    /* ================= WORKFLOW ================= */

    @Override
    public void acknowledgeCommand(Long commandId) {

        Command command = getCommand(commandId);

        if (command.getStatus() != CommandStatus.ISSUED) {
            throw new IllegalStateException(
                    "Only ISSUED commands can be acknowledged");
        }

        command.setStatus(CommandStatus.ACKNOWLEDGED);
        command.setAcknowledgedAt(LocalDateTime.now());

        safeTelemetry(command, "COMMAND_ACKNOWLEDGED");
    }

    @Override
    public void executeCommand(Long commandId) {

        Command command = getCommand(commandId);
        User executor = securityUtil.getCurrentUser();

        if (command.getAssignedTo() != null &&
                !command.getAssignedTo().getId().equals(executor.getId())) {
            throw new IllegalStateException(
                    "Only the assigned executor may execute this command");
        }

        if (command.getExecuteBy() != null &&
                LocalDateTime.now().isAfter(command.getExecuteBy())) {

            command.setStatus(CommandStatus.EXPIRED);
            commandRepository.save(command);

            safeTelemetry(command, "COMMAND_EXPIRED");

            throw new IllegalStateException(
                    "Command execution window expired");
        }

        if (!command.isEmergency() &&
                command.getStatus() != CommandStatus.ACKNOWLEDGED) {
            throw new IllegalStateException(
                    "Command must be ACKNOWLEDGED before execution");
        }

        command.setExecutedBy(executor);
        command.setExecutedAt(LocalDateTime.now());
        command.setStatus(CommandStatus.IN_EXECUTION);

        safeTelemetry(command, "COMMAND_EXECUTED");
    }

    @Override
    public void completeCommand(Long commandId) {

        Command command = getCommand(commandId);

        if (command.getStatus() != CommandStatus.IN_EXECUTION) {
            throw new IllegalStateException(
                    "Only IN_EXECUTION commands can be completed");
        }

        command.setStatus(CommandStatus.COMPLETED);

        safeTelemetry(command, "COMMAND_COMPLETED");
    }

    @Override
    public void failCommand(Long commandId, String reason) {

        Command command = getCommand(commandId);

        if (command.getStatus() != CommandStatus.IN_EXECUTION) {
            throw new IllegalStateException(
                    "Only IN_EXECUTION commands can fail");
        }

        command.setStatus(CommandStatus.FAILED);
        command.setFailureReason(reason);

        safeTelemetry(command, "COMMAND_FAILED");
    }

    @Override
    public void cancelCommand(Long commandId) {

        Command command = getCommand(commandId);

        if (command.getStatus() == CommandStatus.COMPLETED ||
                command.getStatus() == CommandStatus.FAILED) {
            throw new IllegalStateException(
                    "Completed or failed commands cannot be cancelled");
        }

        command.setStatus(CommandStatus.CANCELLED);

        safeTelemetry(command, "COMMAND_CANCELLED");
    }

    /* ================= INTERNAL ================= */

    private Command getCommand(Long commandId) {
        return commandRepository.findById(commandId)
                .orElseThrow(() ->
                        new CommandNotFoundException(
                                "Command not found: " + commandId));
    }

    private void safeTelemetry(Command command, String event) {
        try {
            telemetryService.publishCommandEvent(command, event);
        } catch (Exception ex) {
            // Telemetry must NEVER break command lifecycle
            System.err.println(
                    "Telemetry failure [" + event + "] for command "
                            + command.getCommandCode());
        }
    }
}
