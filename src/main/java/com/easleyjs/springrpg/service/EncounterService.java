package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.InvalidGameActionException;
import com.easleyjs.springrpg.exception.ResourceNotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.MonsterRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class EncounterService {
    private final EncounterRepo encRepo;
    private final MonsterRepo monsterRepo;

    public EncounterService(
            EncounterRepo encRepo,
            MonsterRepo monsterRepo) {
        this.encRepo = encRepo;
        this.monsterRepo = monsterRepo;
    }

    public Encounter create() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        if (pc.getLocation() != Location.FOREST) {
            throw new InvalidGameActionException("Must be in Forest to fight.");
        }

        // Check for existing encounter for that player so that we don't start additional encounters.
        Encounter encounter = encRepo.findByPlayerIdAndStatus(
                pc.getId(),
                EncounterStatus.ACTIVE
        ).orElseGet(() -> {
            Encounter newEncounter = new Encounter();
            newEncounter.setPlayerId(pc.getId());
            newEncounter.setPlayerHp(user.getPlayer().getHealth());

            return newEncounter;
        });

        List<Monster> pool = monsterRepo.findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(
                pc.getLevel(),
                pc.getLevel()
        );

        Monster monster = pool.get(
                new Random().nextInt(pool.size())
        );

        EncounterMonster em = new EncounterMonster();
        em.setName(monster.getName());
        em.setCurrentHealth(monster.getBaseHealth());
        em.setDamage(monster.getBaseDamage());
        em.setXp(monster.getXp());
        em.setEncounter(encounter);

        encounter.getMonsters().add(em);
        encounter.setStatus(EncounterStatus.ACTIVE);

        return encRepo.save(encounter);
    }

    public Encounter getEncounter(long id) {
        return encRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Encounter with id " + id + " not found", id)));
    }

    public List<Encounter> getAllEncounters() {
        return encRepo.findAll();
    }
}
