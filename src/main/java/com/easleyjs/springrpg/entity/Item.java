package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int damageBonus;
    private int damageMultiplier;
    private int defenseBonus;
    private int price;

    @Enumerated(EnumType.STRING)
    ItemType itemType;

    public Item(
            String name,
            int damageBonus,
            int damageMultiplier,
            int defenseBonus,
            int price,
            ItemType itemType) {
        this.name = name;
        this.damageBonus = damageBonus;
        this.damageMultiplier = damageMultiplier;
        this.defenseBonus = defenseBonus;
        this.price = price;
        this.itemType = itemType;
    }
}
