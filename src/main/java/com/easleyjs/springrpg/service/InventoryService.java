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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public List<InventoryItem> getAllByPlayerId(long pcId) {
        //List<InventoryItem> invItems =
        return invRepo.findAllByPlayerId(pcId);
    }

    public EquipResponse addInventoryItem(EquipRequest req) {
        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item Not Found"));

        PlayerCharacter pc = playerRepo.findById(req.getPlayerId())
                .orElseThrow(() -> new NotFoundException("Player not found."));

        InventoryItem inventoryItem = invRepo.save(
                new InventoryItem(
                    pc,
                    item,
                    1,
                    false
        ));

        return new EquipResponse(
                inventoryItem.getId(),
                inventoryItem.getItem().getName(),
                pc.getId()
        );
    }

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

        List<InventoryItem> items = invRepo.findAllByPlayerId(pc.getId());

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
