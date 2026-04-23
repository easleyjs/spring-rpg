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
    long pcId;
    int quantity;

    public EquipResponse(
            long equipId,
            String itemName,
            long pcId,
            int quantity
    ) {
        this.equipId = equipId;
        this.itemName = itemName;
        this.pcId = pcId;
        this.quantity = quantity;
    }
}
