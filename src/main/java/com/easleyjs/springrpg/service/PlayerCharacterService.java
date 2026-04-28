package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.MoveRequest;
import com.easleyjs.springrpg.dto.PlayerCharacterResponse;
import com.easleyjs.springrpg.dto.PlayerMoveResponse;
import com.easleyjs.springrpg.dto.createPlayerRequest;
import com.easleyjs.springrpg.entity.*;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
        player.setLocation(Location.TOWN);
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

    public Page<PlayerCharacterResponse> getAllCharacters(
            int page,
            int size
    ) {
        if (page < 1) page = 0;
        if (size < 1) size = 1;

        int safeSize = Math.min(size, 50);

        PageRequest pageable = PageRequest.of(page, safeSize);

        return playerRepo.findAll(pageable)
                .map(this::toResponse);
    }

    public PlayerCharacterResponse getCharacterById() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        return new PlayerCharacterResponse(
                pc.getId(),
                pc.getName(),
                pc.getXp(),
                pc.getLevel(),
                pc.getHealth(),
                pc.getLocation(),
                pc.getGold()
        );
    }

    public PlayerMoveResponse moveCharacter(MoveRequest req) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PlayerCharacter pc = user.getPlayer();

        pc.setLocation(req.getLocation());

        playerRepo.save(pc);

        return new PlayerMoveResponse(
                req.getLocation()
        );
    }

    PlayerCharacterResponse toResponse(PlayerCharacter pc) {
        return new PlayerCharacterResponse(
                pc.getId(),
                pc.getName(),
                pc.getXp(),
                pc.getLevel(),
                pc.getHealth(),
                pc.getLocation(),
                pc.getGold()
        );
    }

}
