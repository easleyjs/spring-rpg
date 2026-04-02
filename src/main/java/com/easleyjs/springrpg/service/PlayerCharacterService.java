package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlayerCharacterService {
    private final PlayerCharacterRepo repo;

    public PlayerCharacterService(PlayerCharacterRepo repo) {
        this.repo = repo;
    }

    public PlayerCharacter createCharacter(PlayerCharacter pc) {
        return repo.save(pc);
    }

    public List<PlayerCharacter> getAllCharacters() {
        return repo.findAll();
    }

    public PlayerCharacter getCharacterById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Character with id %d not found", id)));
    }

}
