package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.InvalidStateException;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.InventoryRepo;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

import java.text.Format;

@Service
public class CombatService {
    private final PlayerCharacterRepo pcRepo;
    private final InventoryRepo invRepo;
    private final EncounterRepo encRepo;

    public CombatService(
            PlayerCharacterRepo pcRepo,
            InventoryRepo invRepo,
            EncounterRepo encRepo) {
        this.pcRepo = pcRepo;
        this.invRepo = invRepo;
        this.encRepo = encRepo;
    }

    public CombatResult attack(long encounterId) {
        String message;
        Encounter enc = encRepo.findById(encounterId)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Encounter with id %d not found", encounterId)));

        if (enc.getStatus() != EncounterStatus.ACTIVE) {
            throw new InvalidStateException("Encounter with id " + encounterId + " is not ACTIVE");
        }

        PlayerCharacter pc = pcRepo.findById(enc.getPlayerId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("PlayerCharacter with id %d not found", enc.getPlayerId())
                ));

        int attackDamage = calculateDamage(pc);
        int monsterDamage = 10;

        System.out.println("Attack happend in encounter:" + encounterId);
        System.out.println("Damage: " + attackDamage);
        System.out.println("Monster Damage: " + monsterDamage);

        applyPlayerAttack(enc, attackDamage);

        if (enc.getMonsterHp() == 0) {
            enc.setStatus(EncounterStatus.WON);
            encRepo.save(enc);
            System.out.println("Encounter" + encounterId + " won");
            message = String.format(
                    "You attack %s for %d damage.\n%s is dead.\nYou gained +10 XP",
                    "Monster Name", attackDamage, "Monster Name");

            pc.setXp(pc.getXp() + 10);
            if (pc.getXp() >= 100) {
                pc.setLevel(pc.getLevel() + 1);
                pc.setXp(pc.getXp() - 100);
            }
            pcRepo.save(pc);

            return new CombatResult(
                    enc.getPlayerHp(),
                    enc.getMonsterHp(),
                    attackDamage,
                    message,
                    enc.getStatus());
        } else {
            message = String.format(
                    "You attack %s for %d damage.",
                    "Monster Name", attackDamage);

            applyMonsterAttack(enc, monsterDamage);

            if (enc.getPlayerHp() == 0) {
                enc.setStatus(EncounterStatus.LOST);
                System.out.println("Encounter" + encounterId+ " lost");
                message += String.format(
                        "\n%s attacks you for %d damage.\nYou are dead.",
                        "Monster Name", monsterDamage);
            } else {
                message += String.format(
                        "\n%s attacks you for %d damage.",
                        enc.getMonsterId(), monsterDamage);
            }
        }
        encRepo.save(enc);
        return new CombatResult(
                enc.getPlayerHp(),
                enc.getMonsterHp(),
                attackDamage,
                message,
                enc.getStatus());
    }

    private int calculateDamage(PlayerCharacter pc) {
        int baseAttack = pc.getLevel() * 5;

        InventoryItem invWeapon = invRepo.findByPlayerIdAndEquippedTrueAndItemType(
                pc.getId(),
                ItemType.WEAPON).orElseThrow(
                        () -> new NotFoundException("Weapon not found for player."));

        int weaponFlatBonus = invWeapon.getItem().getDamageBonus();
        int weaponDmgMultiplier = invWeapon.getItem().getDamageMultiplier();

        return (int)((baseAttack + weaponFlatBonus)
                * weaponDmgMultiplier);
    }

    private void applyPlayerAttack(Encounter enc, int damage) {
        enc.setMonsterHp(Math.max(0,enc.getMonsterHp() - damage));
    }

    private void applyMonsterAttack(Encounter enc, int damage) {
        enc.setPlayerHp(Math.max(0, enc.getPlayerHp() - damage));
    }
}
