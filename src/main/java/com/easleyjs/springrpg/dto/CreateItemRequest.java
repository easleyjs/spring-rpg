package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateItemRequest {
    String name;
    String description;
    int damageBonus;
    int damageMultiplier;
    int defenseBonus;
    int price;
    ItemType itemType;
    boolean shopItem;
}
