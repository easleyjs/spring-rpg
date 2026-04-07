package com.easleyjs.springrpg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int xp;
    private int level;
    private int health;
    private String weapon;
    private int weaponFlatBonus;
    private int weaponDmgMultiplier;
    private String armor;

    public PlayerCharacter(String name) {
        this.name = name;
        this.xp = 0;
        this.level = 1;
        this.health = 100;
        this.weapon = "Wooden Stick";
        this.weaponFlatBonus = 0;
        this.weaponDmgMultiplier = 1;
        this.armor = "Peasant Outfit";
    }
}
