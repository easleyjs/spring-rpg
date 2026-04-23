package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    ItemRepo itemRepo;
    InventoryRepo inventoryRepo;

    public ShopService(ItemRepo itemRepo, InventoryRepo inventoryRepo) {
        this.itemRepo = itemRepo;
        this.inventoryRepo = inventoryRepo;
    }

    public List<ShopItemResponse> getItems() {
        return itemRepo.findByShopItem(true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ShopItemResponse toResponse(Item i) {
        return new ShopItemResponse(
                i.getName(),
                i.getDescription(),
                i.getItemType(),
                i.getPrice()
        );
    }
}
