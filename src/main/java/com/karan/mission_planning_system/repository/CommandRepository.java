package com.karan.mission_planning_system.repository;

import com.karan.mission_planning_system.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    Optional<Command> findByCommandCode(String commandCode);

    List<Command> findAllByMissionId(Long missionId);

    boolean existsByCommandCode(String commandCode);
}
