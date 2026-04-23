package com.easleyjs.springrpg.service;

import com.easleyjs.springrpg.dto.*;
import com.easleyjs.springrpg.entity.PlayerCharacter;
import com.easleyjs.springrpg.entity.User;
import com.easleyjs.springrpg.exception.NotFoundException;
import com.easleyjs.springrpg.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final PlayerCharacterService pcService;
    private final JwtService jwtService;

    public AuthService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            PlayerCharacterService pcService, JwtService jwtService
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.pcService = pcService;
        this.jwtService = jwtService;
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

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if  (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token
        );
    }
}
