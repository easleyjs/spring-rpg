package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EquipRequest;
import com.easleyjs.springrpg.dto.EquipResponse;
import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.ItemType;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;

import java.util.List;

public class InventoryService {
    InventoryRepo invRepo;
    ItemRepo itemRepo;
    PlayerCharacterRepo playerRepo;

    public InventoryService(InventoryRepo invRepo,
                            ItemRepo itemRepo,
                            PlayerCharacterRepo playerRepo) {
        this.invRepo = invRepo;
        this.itemRepo = itemRepo;
        this.playerRepo = playerRepo;
    }

    //to-do: make CharacterInventory DTO for this
    public List<InventoryItem> getById(long pcId) {
        //List<InventoryItem> invItems =
        return invRepo.findByPlayerId(pcId);
    }
    /*
    TO-DO: addInventoryItem
     */
    public EquipResponse equipInventoryItem(EquipRequest req) {
        InventoryItem item = invRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not in inventory"));

        PlayerCharacter pc = playerRepo.findById(req.getPlayerId())
                .orElseThrow(() -> new NotFoundException("Player not found"));

        if(!(item.getPlayer().getId().equals(pc.getId()))) {
            throw new RuntimeException("Item does not belong to this player");
        }

        if (!(item.getItem().getItemType() == ItemType.WEAPON)) {
            throw new RuntimeException("Cannot equip non-weapons");
        }

        List<InventoryItem> items = invRepo.findByPlayerId(pc.getId());

        items.stream()
                .filter(ii -> ii.getItem().getItemType() == ItemType.WEAPON)
                .forEach(ii -> ii.setEquipped(false));

        item.setEquipped(true);

        invRepo.saveAll(items);

        return new EquipResponse(
                item.getId(),
                item.getItem().getName(),
                pc.getId()
        );
    }
}
