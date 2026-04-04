package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.EncounterRepo;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

@Service
public class CombatService {
    private final PlayerCharacterRepo pcRepo;
    private final EncounterRepo encRepo;

    public CombatService(PlayerCharacterRepo pcRepo, EncounterRepo encRepo) {
        this.pcRepo = pcRepo;
        this.encRepo = encRepo;
    }

    public CombatResult attack(long encounterId) {
        Encounter enc = encRepo.findById(encounterId)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Encounter with id %d not found", encounterId)));
        int damage = 10;
        String message = String.format(
                "%s attacks for %d damage.", enc.getPlayerId(), damage);

        return new CombatResult(damage, message);
    }
}
