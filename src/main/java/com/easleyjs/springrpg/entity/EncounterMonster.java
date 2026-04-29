package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class EncounterMonster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int currentHealth;
    private int damage;
    private int xp;
    private int gold;

    @ManyToOne
    private Encounter encounter;
}
