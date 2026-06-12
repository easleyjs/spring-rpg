package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.EncounterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CombatResult {
    public int playerHp;
    public int monsterHp;
    public int damage;
    public List<String> messages;
    EncounterStatus status;
}
