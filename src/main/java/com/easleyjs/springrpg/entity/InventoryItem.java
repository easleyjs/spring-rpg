package com.easleyjs.springrpg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private PlayerCharacter player;
    @ManyToOne(optional = false)
    private Item item;
    private int quantity;
    private boolean equipped;

    public InventoryItem(PlayerCharacter player, Item item, int quantity, boolean equipped) {
        this.player = player;
        this.item = item;
        this.quantity = quantity;
        this.equipped = equipped;
    }
}
