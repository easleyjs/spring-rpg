package com.easleyjs.springrpg.controller;

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
    public PlayerCharacter create(@RequestBody PlayerCharacter pc) {
        return service.createCharacter(pc);
    }

    @GetMapping
    List<PlayerCharacter> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAllCharacters(page, size).getContent();
    }

    @GetMapping("/{id}")
    public PlayerCharacter getById(@PathVariable Long id) {
        return service.getCharacterById(id);
    }

}
