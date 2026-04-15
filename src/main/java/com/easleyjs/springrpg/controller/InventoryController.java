package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.repository.InventoryRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    InventoryRepo inventoryRepo;

    public InventoryController(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    @GetMapping("/{id}")
    public CombatResult getInventory(@PathVariable String id) {

    }
}
