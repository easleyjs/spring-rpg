package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EncounterService {
    private final EncounterRepo encRepo;
    private final UserRepo userRepo;

    public EncounterService(
            EncounterRepo encRepo,
            UserRepo userRepo) {
        this.encRepo = encRepo;
        this.userRepo = userRepo;
    }

    public Encounter create() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        if (pc.getLocation() != Location.FOREST) {
            throw new RuntimeException("Must be in Forest to fight.");
        }

        Encounter encounter = new Encounter(pc.getId());
        encounter.setPlayerHp(user.getPlayer().getHealth());
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
