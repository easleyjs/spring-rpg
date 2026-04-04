package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EncounterStartRequest;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class EncounterService {
    private EncounterRepo encRepo;
    private PlayerCharacterRepo pcRepo;

    public EncounterService(EncounterRepo encRepo,  PlayerCharacterRepo pcRepo) {
        this.encRepo = encRepo;
        this.pcRepo = pcRepo;
    }

    public Encounter create(long playerId) {
        PlayerCharacter pc = pcRepo.findById(playerId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Character with id %d not found", playerId)));
        Encounter encounter = new Encounter(pc.getId());
        encounter.setPlayerHp(1);
        encounter.setMonsterHp(0);
        encounter.setMonsterId(1);
        encounter.setStatus("ACTIVE");

        return encRepo.save(encounter);
    }
}
