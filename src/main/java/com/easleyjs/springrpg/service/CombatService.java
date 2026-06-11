package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.InvalidGameActionException;
import com.easleyjs.springrpg.exception.ResourceNotFoundException;
import com.easleyjs.springrpg.repository.EncounterRepo;
import com.easleyjs.springrpg.repository.InventoryRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

import java.util.ArrayList;
import java.util.List;

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

    public CombatResult attack() {
        List<String> messages = new ArrayList<>();

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        if (pc.getLocation() != Location.FOREST) {
            throw new InvalidGameActionException("Must be in Forest to fight.");
        }

        Encounter enc = encRepo.findByPlayerIdAndStatus(pc.getId(), EncounterStatus.ACTIVE)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("No Active Encounters found for player.")));

        InventoryItem invWeapon = invRepo.findByPlayerIdAndEquippedTrueAndItem_ItemType(
                pc.getId(),
                ItemType.WEAPON).orElseThrow(
                        () -> new ResourceNotFoundException("Weapon not found for player"));
        int attackDamage = calculateDamage(pc);

        EncounterMonster em = enc.getMonsters().getFirst();

        String monsterName = em.getName();
        int monsterDamage = em.getDamage();

        applyPlayerAttack(em, attackDamage);

        if (em.getCurrentHealth() == 0) {
            enc.setStatus(EncounterStatus.WON);
            encRepo.save(enc);

            messages.add(String.format(
                    "You attack %s for %d damage.",
                    monsterName, attackDamage));
            messages.add(String.format(
                    "%s is dead.", monsterName
            ));
            messages.add(String.format("You gained +%s XP", em.getXp()));

            pc.setXp(pc.getXp() + 10);
            if (pc.getXp() >= 100) {
                pc.setLevel(pc.getLevel() + 1);
                pc.setXp(pc.getXp() - 100);
            }
            pcRepo.save(pc);

            return new CombatResult(
                    enc.getPlayerHp(),
                    em.getCurrentHealth(),
                    attackDamage,
                    messages,
                    enc.getStatus());
        } else {
            messages.add(String.format(
                    "You attack %s with %s for %d damage.",
                    monsterName, invWeapon.getItem().getName(), attackDamage));

            applyMonsterAttack(enc, monsterDamage);

            if (enc.getPlayerHp() == 0) {
                enc.setStatus(EncounterStatus.LOST);

                messages.add(String.format(
                        "%s attacks you for %d damage.",
                        monsterName, monsterDamage));
                messages.add("You are dead.");
            } else {
                messages.add(String.format(
                        "%s attacks you for %d damage.",
                        monsterName, monsterDamage));
            }
        }
        encRepo.save(enc);

        return new CombatResult(
                enc.getPlayerHp(),
                em.getCurrentHealth(),
                attackDamage,
                messages,
                enc.getStatus());
    }

    private int calculateDamage(PlayerCharacter pc) {
        int baseAttack = pc.getLevel() * 5;

        InventoryItem invWeapon = invRepo.findByPlayerIdAndEquippedTrueAndItem_ItemType(
                pc.getId(),
                ItemType.WEAPON).orElseThrow(
                        () -> new ResourceNotFoundException("Weapon not found for player."));

        int weaponFlatBonus = invWeapon.getItem().getDamageBonus();
        int weaponDmgMultiplier = invWeapon.getItem().getDamageMultiplier();

        return ((baseAttack + weaponFlatBonus)
                * weaponDmgMultiplier);
    }

    private void applyPlayerAttack(EncounterMonster em, int damage) {
        em.setCurrentHealth(Math.max(0, em.getCurrentHealth() - damage));
        System.out.println(em.getCurrentHealth());
    }

    private void applyMonsterAttack(Encounter enc, int damage) {
        enc.setPlayerHp(Math.max(0, enc.getPlayerHp() - damage));
    }
}
