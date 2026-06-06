package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.CreateItemRequest;
import com.easleyjs.springrpg.dto.CreateItemResponse;
import com.easleyjs.springrpg.dto.ItemDetailResponse;
import com.easleyjs.springrpg.dto.PlayerCharacterResponse;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.ItemRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepo itemRepo;

    public ItemService(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    public Page<ItemDetailResponse> getAllItems(
            int page,
            int size
    ) {
        if (page < 1) page = 0;
        if (size < 1) size = 1;

        int safeSize = Math.min(size, 50);

        PageRequest pageable = PageRequest.of(
                page,
                safeSize,
                Sort.by(Sort.Direction.ASC, "id")
        );

        return itemRepo.findAll(pageable)
                .map(this::toResponse);
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

    ItemDetailResponse toResponse(Item item) {
        return new ItemDetailResponse(
            item.getName(),
            item.getDescription(),
            item.getDamageBonus(),
            item.getDamageMultiplier(),
            item.getDefenseBonus(),
            item.getPrice(),
            item.getItemType(),
            item.isShopItem()
        );
    }
}
