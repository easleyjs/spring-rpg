package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.entity.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InventoryItemResponse {
    private Long id;
    private String itemName;
    private ItemType itemType;
    private int damage;
    private int quantity;
    private boolean equipped;

    public InventoryItemResponse(InventoryItem ii) {
        this.id = ii.getId();
        this.itemName = ii.getItem().getName();
        this.itemType = ii.getItem().getItemType();
        this.damage = ii.getItem().getDamageMultiplier();
        this.quantity = ii.getQuantity();
        this.equipped = ii.isEquipped();
    }
}