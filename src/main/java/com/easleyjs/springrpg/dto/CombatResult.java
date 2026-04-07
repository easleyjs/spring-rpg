package com.easleyjs.springrpg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CombatResult {
    public int playerHp;
    public int monsterHp;
    public int damage;
    public String message;
    String status;
}
