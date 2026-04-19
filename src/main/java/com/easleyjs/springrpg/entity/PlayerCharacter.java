package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "player")
    private List<InventoryItem> inventoryItems = new ArrayList<>();

    public PlayerCharacter(String name) {
        this.name = name;
        this.xp = 0;
        this.level = 1;
        this.health = 100;
    }
}
