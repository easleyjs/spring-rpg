package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemDetailResponse {
    private String name;
    private String description;
    private int damageBonus;
    private int damageMultiplier;
    private int defenseBonus;
    private int price;
    private ItemType itemType;
    private boolean shopItem;

    public ItemDetailResponse(
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
        this.damageBonus =  damageBonus;
        this.damageMultiplier = damageMultiplier;
        this.defenseBonus = defenseBonus;
        this.price = price;
        this.itemType = itemType;
        this.shopItem = shopItem;
    }
}