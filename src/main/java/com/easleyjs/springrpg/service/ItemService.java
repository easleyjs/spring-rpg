package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CreateItemRequest;
import com.easleyjs.springrpg.dto.CreateItemResponse;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.repository.ItemRepo;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepo itemRepo;

    public ItemService(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    public CreateItemResponse createItem(CreateItemRequest createItemRequest) {
        Item item = itemRepo.save(new Item(
                createItemRequest.getName(),
                createItemRequest.getDescription(),
                createItemRequest.getDamageBonus(),
                createItemRequest.getDamageMultiplier(),
                createItemRequest.getDefenseBonus(),
                createItemRequest.getPrice(),
                createItemRequest.getItemType(),
                createItemRequest.isShopItem()
        ));

        return new CreateItemResponse(
                item.getName()
        );
    }
}
