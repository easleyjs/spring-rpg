package com.easleyjs.springrpg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@NoArgsConstructor
@Data
public class Monster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int baseHealth;
    private int minLevel;
    private int maxLevel;

    private int baseDamage;
    private int xp;
    private int gold;
}
