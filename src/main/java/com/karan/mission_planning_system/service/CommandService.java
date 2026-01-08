package com.karan.mission_planning_system.service;

import com.karan.mission_planning_system.dto.CommandRequestDto;
import com.karan.mission_planning_system.dto.CommandResponseDto;

import java.util.List;

public interface CommandService {

    CommandResponseDto issueCommand(CommandRequestDto requestDto);

    CommandResponseDto getCommandById(Long commandId);

    CommandResponseDto getCommandByCode(String commandCode);

    List<CommandResponseDto> getCommandsByMission(Long missionId);

    void acknowledgeCommand(Long commandId);

    void executeCommand(Long commandId);

    void completeCommand(Long commandId);

    void failCommand(Long commandId, String reason);

    void cancelCommand(Long commandId);
}
