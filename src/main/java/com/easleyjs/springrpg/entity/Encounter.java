package com.easleyjs.springrpg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    private String status;

    public Encounter(long playerId) {
        this.playerId = playerId;
    }
}
