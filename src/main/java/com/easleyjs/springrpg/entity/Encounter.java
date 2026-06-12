package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Encounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_character_id", nullable = false)
    private PlayerCharacter playerCharacter;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EncounterMonster> monsters = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EncounterStatus status;

    public Encounter(PlayerCharacter player) {
        this.playerCharacter = player;
    }
}
