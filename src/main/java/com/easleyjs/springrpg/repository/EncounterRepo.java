package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.EncounterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EncounterRepo extends
        JpaRepository<Encounter, Long> {
    Optional<Encounter> findByPlayerIdAndStatus(Long playerId, EncounterStatus status);
}
