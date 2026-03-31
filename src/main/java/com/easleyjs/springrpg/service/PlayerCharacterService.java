package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.repository.PlayerCharacterRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerCharacterService {
    private final PlayerCharacterRepo repo;

    private PlayerCharacterService(PlayerCharacterRepo repo) {
        this.repo = repo;
    }

    private PlayerCharacter createCharacter(PlayerCharacter pc) {
        return repo.save(pc);
    }

    public List<PlayerCharacter> getAllCharacters() {
        return repo.findAll();
    }

}
