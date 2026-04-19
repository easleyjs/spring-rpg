package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.dto.EquipRequest;
import com.easleyjs.springrpg.dto.EquipResponse;
import com.easleyjs.springrpg.dto.InventoryItemResponse;
import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.repository.InventoryRepo;
import com.easleyjs.springrpg.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    InventoryService invService;

    public InventoryController(InventoryService invService)
    {
        this.invService = invService;
    }

    @GetMapping("/{id}")
    public List<InventoryItemResponse> getInventory(@PathVariable long id) {
            return invService.getAllByPlayerId(id)
                    .stream()
                    .map(InventoryItemResponse::new)
                    .toList();
    }

    @PostMapping("/add")
    public EquipResponse addItem(@RequestBody EquipRequest equipRequest) {
        return invService.addInventoryItem(equipRequest);
    }

    @PostMapping("/equip")
    public EquipResponse equip(@RequestBody EquipRequest equipRequest) {
        return invService.equipInventoryItem(equipRequest);
    }
}
