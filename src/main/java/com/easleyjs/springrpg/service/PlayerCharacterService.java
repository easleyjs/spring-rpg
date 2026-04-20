package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.PlayerMoveResponse;
import com.easleyjs.springrpg.dto.createPlayerRequest;
import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.Location;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PlayerCharacterService {
    private final PlayerCharacterRepo playerRepo;
    private final ItemRepo itemRepo;
    private final InventoryRepo inventoryRepo;


    public PlayerCharacterService(PlayerCharacterRepo playerRepo,
                                  ItemRepo itemRepo,
                                  InventoryRepo inventoryRepo) {
        this.playerRepo = playerRepo;
        this.itemRepo = itemRepo;
        this.inventoryRepo = inventoryRepo;
    }

    public PlayerCharacter createCharacter(createPlayerRequest req) {
        PlayerCharacter player = new PlayerCharacter();
        player.setName(req.getName());
        playerRepo.save(player);

        // Give player starting gear
        Item starterSword = itemRepo.findByName("Wooden Stick")
                .orElseThrow(() -> new NotFoundException("Item not found."));
        InventoryItem invItem = new InventoryItem();
        invItem.setPlayer(player);
        invItem.setItem(starterSword);
        invItem.setQuantity(1);
        invItem.setEquipped(true);

        inventoryRepo.save(invItem);

        return player;
    }

    public Page<PlayerCharacter> getAllCharacters(
            int page,
            int size
    ) {
        if (page < 1) page = 0;
        if (size < 1) size = 1;

        int safeSize = Math.min(size, 50);
        return playerRepo.findAll(PageRequest.of(page, safeSize));
    }

    public PlayerCharacter getCharacterById(Long id) {
        return playerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Character with id %d not found", id)));
    }

    public PlayerMoveResponse moveCharacter(long pcId, Location location) {
        PlayerCharacter pc = playerRepo.findById(pcId)
                .orElseThrow(() -> new NotFoundException("Player with id " + pcId + " not found"));

        pc.setLocation(location);

        playerRepo.save(pc);

        return new PlayerMoveResponse(
                pcId,
                location
        );
    }

}
