package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerCharacter player;

    public User(
            String username,
            String password,
            PlayerCharacter player
    ) {
        this.username = username;
        this.password = password;
        this.player = player;
    }
}
