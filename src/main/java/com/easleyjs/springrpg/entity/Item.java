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
    private String description;
    private int damageBonus;
    private int damageMultiplier;
    private int defenseBonus;
    private int price;
    private boolean shopItem;

    @Enumerated(EnumType.STRING)
    ItemType itemType;

    public Item(
            String name,
            String description,
            int damageBonus,
            int damageMultiplier,
            int defenseBonus,
            int price,
            ItemType itemType,
            boolean shopItem) {
        this.name = name;
        this.description = description;
        this.damageBonus = damageBonus;
        this.damageMultiplier = damageMultiplier;
        this.defenseBonus = defenseBonus;
        this.price = price;
        this.itemType = itemType;
        this.shopItem = shopItem;
    }
}
