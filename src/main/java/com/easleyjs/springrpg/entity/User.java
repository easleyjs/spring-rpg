package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerCharacter player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    public User(
            String username,
            String password,
            PlayerCharacter player,
            Role role
    ) {
        this.username = username;
        this.password = password;
        this.player = player;
        this.role = role;
    }
}
