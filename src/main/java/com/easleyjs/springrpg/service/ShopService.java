package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EquipRequest;
import com.easleyjs.springrpg.dto.ShopBuyRequest;
import com.easleyjs.springrpg.dto.ShopBuyResponse;
import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.exception.InvalidStateException;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    ItemRepo itemRepo;
    PlayerCharacterRepo pcRepo;
    InventoryService invService;

    public ShopService(
            ItemRepo itemRepo,
            InventoryService invService,
            PlayerCharacterRepo pcRepo) {
        this.itemRepo = itemRepo;
        this.invService = invService;
        this.pcRepo = pcRepo;
    }

    public ShopBuyResponse buyItem(ShopBuyRequest req) {
        PlayerCharacter pc = pcRepo.findById(req.getPlayerId())
                .orElseThrow(() -> new NotFoundException("Player not found"));

        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (pc.getGold() < item.getPrice()) {
            int remaining = item.getPrice() - pc.getGold();
            pc.setGold(remaining);
            pcRepo.save(pc);


            InventoryItem invItem = invService.addInventoryItem(
                    new EquipRequest(
                            item.getId(),
                            pc.getId()
                    )
            );

            return new ShopBuyResponse(
                item.getName(),
        1,
                remaining
            );
        } else {
            throw new InvalidStateException("Not enough gold");
        }

    }

    public List<ShopItemResponse> getItems() {
        return itemRepo.findByShopItem(true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ShopItemResponse toResponse(Item i) {
        return new ShopItemResponse(
                i.getId(),
                i.getName(),
                i.getDescription(),
                i.getItemType(),
                i.getPrice()
        );
    }
}
