package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.EncounterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
public class EncounterStartResponse {
    long encounterId;
    int playerHp;
    String monsterName;
    int monsterHp;
    EncounterStatus status;
}