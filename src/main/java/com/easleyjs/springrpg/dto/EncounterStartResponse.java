package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.EncounterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EncounterStartResponse {
    long encounterId;
    int playerHp;
    int monsterHp;
    EncounterStatus status;
}