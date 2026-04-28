package com.easleyjs.springrpg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
/**
 * Equip item request for equipping intentory items
 * @param itemId the ID of the InventoryItem (represents the specific item instance in the player's inventory)
 * @param playerId the ID of the PlayerCharacter (the player performing the action)
 */
public class EquipRequest {
    long itemId;

    public EquipRequest(long itemId) {
        this.itemId = itemId;
    }
}
