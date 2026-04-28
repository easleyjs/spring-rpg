package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.EquipRequest;
import com.easleyjs.springrpg.dto.ShopBuyRequest;
import com.easleyjs.springrpg.dto.ShopBuyResponse;
import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.Location;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.entity.User;
import com.easleyjs.springrpg.exception.InvalidStateException;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (pc.getLocation() != Location.SHOP) {
            throw new InvalidStateException("You must be in the shop to buy items.");
        }

        if (pc.getGold() < item.getPrice()) {
            int remaining = item.getPrice() - pc.getGold();
            pc.setGold(remaining);
            pcRepo.save(pc);

            invService.addInventoryItem(
                    new EquipRequest(
                            item.getId()
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
