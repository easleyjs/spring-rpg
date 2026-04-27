package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
// TODO: Create a Monster entity/set up relation
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Encounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long playerId;
    private int playerHp;
    private long monsterId;
    private int monsterHp;
    @Enumerated(EnumType.STRING)
    private EncounterStatus status;

    public Encounter(long playerId) {
        this.playerId = playerId;
    }
}
