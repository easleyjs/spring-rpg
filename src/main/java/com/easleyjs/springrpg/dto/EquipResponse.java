package com.easleyjs.springrpg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class EquipResponse {
    long  equipId;
    String itemName;
    int quantity;

    public EquipResponse(
            long equipId,
            String itemName,
            int quantity
    ) {
        this.equipId = equipId;
        this.itemName = itemName;
        this.quantity = quantity;
    }
}
