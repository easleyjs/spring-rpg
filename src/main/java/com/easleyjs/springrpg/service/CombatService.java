package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.InvalidStateException;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.InventoryRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

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

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        if (pc.getLocation() != Location.FOREST) {
            throw new RuntimeException("Must be in Forest to fight.");
        }

        InventoryItem invWeapon = invRepo.findByPlayerIdAndEquippedTrueAndItem_ItemType(
                pc.getId(),
                ItemType.WEAPON).orElseThrow(
                        () -> new NotFoundException("Weapon not found for player"));
        int attackDamage = calculateDamage(pc);

        EncounterMonster em = enc.getMonsters().get(0);

        String monsterName = em.getName();
        int monsterDamage = em.getDamage();
        int monsterHp = em.getCurrentHealth();

        applyPlayerAttack(enc, attackDamage);

        if (monsterHp == 0) {
            enc.setStatus(EncounterStatus.WON);
            encRepo.save(enc);

            message = String.format(
                    "You attack %s for %d damage.\n%s is dead.\nYou gained +10 XP",
                    monsterName, attackDamage, monsterName);

            pc.setXp(pc.getXp() + 10);
            if (pc.getXp() >= 100) {
                pc.setLevel(pc.getLevel() + 1);
                pc.setXp(pc.getXp() - 100);
            }
            pcRepo.save(pc);

            return new CombatResult(
                    enc.getPlayerHp(),
                    monsterHp,
                    attackDamage,
                    message,
                    enc.getStatus());
        } else {
            message = String.format(
                    "You attack %s with %s for %d damage.",
                    monsterName, invWeapon.getItem().getName(), attackDamage);

            applyMonsterAttack(enc, monsterDamage);

            if (enc.getPlayerHp() == 0) {
                enc.setStatus(EncounterStatus.LOST);
                System.out.println("Encounter" + encounterId+ " lost");
                message += String.format(
                        "\n%s attacks you for %d damage.\nYou are dead.",
                        monsterName, monsterDamage);
            } else {
                message += String.format(
                        "\n%s attacks you for %d damage.",
                        monsterName, monsterDamage);
            }
        }
        encRepo.save(enc);
        return new CombatResult(
                enc.getPlayerHp(),
                monsterHp,
                attackDamage,
                message,
                enc.getStatus());
    }

    private int calculateDamage(PlayerCharacter pc) {
        int baseAttack = pc.getLevel() * 5;

        InventoryItem invWeapon = invRepo.findByPlayerIdAndEquippedTrueAndItem_ItemType(
                pc.getId(),
                ItemType.WEAPON).orElseThrow(
                        () -> new NotFoundException("Weapon not found for player."));

        int weaponFlatBonus = invWeapon.getItem().getDamageBonus();
        int weaponDmgMultiplier = invWeapon.getItem().getDamageMultiplier();

        return (int)((baseAttack + weaponFlatBonus)
                * weaponDmgMultiplier);
    }

    private void applyPlayerAttack(Encounter enc, int damage) {
        EncounterMonster em = enc.getMonsters().get(0);
        em.setCurrentHealth(Math.max(0, em.getCurrentHealth() - damage));
    }

    private void applyMonsterAttack(Encounter enc, int damage) {
        enc.setPlayerHp(Math.max(0, enc.getPlayerHp() - damage));
    }
}
