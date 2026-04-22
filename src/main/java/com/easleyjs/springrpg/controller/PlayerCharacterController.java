package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.MoveRequest;
import com.easleyjs.springrpg.dto.PlayerCharacterResponse;
import com.easleyjs.springrpg.dto.PlayerMoveResponse;
import com.easleyjs.springrpg.dto.createPlayerRequest;
import com.easleyjs.springrpg.entity.Location;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.service.PlayerCharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class PlayerCharacterController {
    PlayerCharacterService service;

    public PlayerCharacterController(PlayerCharacterService service) {
        this.service = service;
    }

    @PostMapping
    public PlayerCharacter create(@RequestBody createPlayerRequest pc) {
        return service.createCharacter(pc);
    }

    @GetMapping
    List<PlayerCharacterResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAllCharacters(page, size).getContent();
    }

    @GetMapping("/{id}")
    public PlayerCharacterResponse getById(@PathVariable Long id) {
        return service.getCharacterById(id);
    }

    @PostMapping("/move")
    public PlayerMoveResponse move(
            @RequestBody MoveRequest req) {
        return service.moveCharacter(req);
    }

}
