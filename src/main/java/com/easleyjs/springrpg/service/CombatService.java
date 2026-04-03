package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import org.springframework.stereotype.Service;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

@Service
public class CombatService {
    private final PlayerCharacterRepo pcRepo;

    public CombatService(PlayerCharacterRepo pcRepo) {
        this.pcRepo = pcRepo;
    }

    public CombatResult attack(long pcId) {
        PlayerCharacter pc = pcRepo.findById(pcId)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Character with id %d not found", pcId)));
        int damage = 10;
        String message = String.format(
                "%s attacks for %d damage.", pc.getName(), damage);

        return new CombatResult(damage, message);
    }
}
