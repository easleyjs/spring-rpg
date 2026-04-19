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

    public EquipResponse(long equipId, String itemName, long pcId) {
        this.equipId = equipId;
        this.itemName = itemName;
        this.pcId = pcId;
    }
}
