package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.EncounterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterRepo extends
        JpaRepository<Encounter, Long> {
    Encounter findByPlayerIdAndStatus(Long playerId, EncounterStatus status);
}
