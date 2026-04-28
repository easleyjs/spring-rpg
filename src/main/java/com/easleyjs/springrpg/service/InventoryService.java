package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EquipRequest;
import com.easleyjs.springrpg.dto.EquipResponse;
import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<InventoryItem> getAllByPlayerId() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        return invRepo.findAllByPlayerId(pc.getId());
    }

    public EquipResponse addInventoryItem(EquipRequest req) {
        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item Not Found"));

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        InventoryItem invItem;

        Optional<InventoryItem> optItem = invRepo.findByPlayerIdAndItemId(
                pc.getId(), item.getId());

        if (optItem.isPresent()) {
            invItem = optItem.get();
            invItem.setQuantity(invItem.getQuantity() + 1);
        } else {
            invItem = new InventoryItem(
                    pc,
                    item,
                    1,
                    false
            );
        }

        invRepo.save(invItem);

        return new EquipResponse(
                invItem.getId(),
                invItem.getItem().getName(),
                1
        );
    }

    public EquipResponse equipInventoryItem(EquipRequest req) {
        InventoryItem item = invRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not in inventory"));

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

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
                item.getQuantity()
        );
    }
}
