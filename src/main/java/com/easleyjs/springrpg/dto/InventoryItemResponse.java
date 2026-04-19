package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.InventoryItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InventoryItemResponse {
    private Long id;
    private String itemName;
    private int quantity;
    private boolean equipped;

    public InventoryItemResponse(InventoryItem ii) {
        this.id = ii.getId();
        this.itemName = ii.getItem().getName();
        this.quantity = ii.getQuantity();
        this.equipped = ii.isEquipped();
    }
}