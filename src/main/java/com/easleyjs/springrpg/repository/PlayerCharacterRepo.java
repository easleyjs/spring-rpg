package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCharacterRepo
        extends JpaRepository<PlayerCharacter, Long> {
}
