package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.EncounterRepo;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

import java.util.Objects;

@Service
public class CombatService {
    private final PlayerCharacterRepo pcRepo;
    private final EncounterRepo encRepo;

    public CombatService(PlayerCharacterRepo pcRepo, EncounterRepo encRepo) {
        this.pcRepo = pcRepo;
        this.encRepo = encRepo;
    }

    public CombatResult attack(long encounterId) {
        String message = "";
        Encounter enc = encRepo.findById(encounterId)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Encounter with id %d not found", encounterId)));

        if (!"ACTIVE".equals(enc.getStatus())) {
            throw new RuntimeException("Encounter with id " + encounterId + " is not ACTIVE");
        }

        int playerHp = enc.getPlayerHp();
        int attackDamage = 10;
        int monsterHp = enc.getMonsterHp();
        int monsterDamage = 10;

        if ((monsterHp - attackDamage) < 1) {
            enc.setStatus("WON");
            encRepo.save(enc);

            message = String.format(
                    "You attack %s for %d damage.\n%s is dead.",
                    "Monster Name", attackDamage, "Monster Name");

            return new CombatResult(attackDamage, message, enc.getStatus());
        } else {
            enc.setMonsterHp(monsterHp - attackDamage);

            message = String.format(
                    "You attack %s for %d damage.",
                    "Monster Name", attackDamage);

            if (playerHp - monsterDamage < 1) {
                enc.setStatus("LOST");
                enc.setPlayerHp(0);

                message += String.format(
                        "\n%s attacks you for %d damage.\nYou are dead.",
                        "Monster Name", monsterDamage);
            } else {
                enc.setPlayerHp(playerHp - monsterDamage);

                message += String.format(
                        "\n%s attacks you for %d damage.",
                        enc.getMonsterId(), monsterDamage);
            }
        }

        return new CombatResult(attackDamage, message, enc.getStatus());
    }
}
