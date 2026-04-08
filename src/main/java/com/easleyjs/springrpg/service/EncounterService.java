package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EncounterStartRequest;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.EncounterStatus;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
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
                .orElseThrow(() -> new NotFoundException(
                        String.format("Character with id %d not found", playerId)));
        Encounter encounter = new Encounter(pc.getId());
        encounter.setPlayerHp(100);
        encounter.setMonsterHp(30);
        encounter.setMonsterId(1);
        encounter.setStatus(EncounterStatus.ACTIVE);

        return encRepo.save(encounter);
    }

    public Encounter getEncounter(long id) {
        Encounter encounter = encRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Encounter with id " + id + " not found", id)));
        return encounter;
    }

    public List<Encounter> getAllEncounters() {
        return encRepo.findAll();
    }
}
