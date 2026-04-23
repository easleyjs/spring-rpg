package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.RegisterRequest;
import com.easleyjs.springrpg.dto.RegisterResponse;
import com.easleyjs.springrpg.dto.createPlayerRequest;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.entity.User;
import com.easleyjs.springrpg.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final PlayerCharacterService pcService;

    public AuthService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            PlayerCharacterService pcService
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.pcService = pcService;
    }

    public RegisterResponse register(RegisterRequest req) {
        String hashed = passwordEncoder.encode(req.getPassword());

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(hashed);

        PlayerCharacter pc = pcService.createCharacter(
                new createPlayerRequest(
                    req.getCharacterName()
                )
        );
        user.setPlayer(pc);

        userRepo.save(user);

        return new RegisterResponse(
                user.getUsername(),
                user.getPassword(),
                pc.getName()
        );
    }
}
