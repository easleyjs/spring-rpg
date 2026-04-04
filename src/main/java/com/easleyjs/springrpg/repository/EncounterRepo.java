package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterRepo extends
        JpaRepository<Encounter, Long> {}
