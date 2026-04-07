package com.easleyjs.springrpg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EncounterStartResponse {
    long encounterId;
    int playerHp;
    int monsterHp;
    String status;
}