package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.AttackRequest;
import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.dto.EncounterStartRequest;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.service.CombatService;
import com.easleyjs.springrpg.service.EncounterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/combat")
public class CombatController {
    private final CombatService combatService;
    private final EncounterService encounterService;

    public CombatController(CombatService combatService, EncounterService encounterService) {
        this.combatService = combatService;
        this.encounterService = encounterService;
    }

    @PostMapping("/create")
    public Encounter create(@RequestBody EncounterStartRequest request) {
        return encounterService.create(request.getPlayerId());
    }

    @PostMapping("/attack")
    public CombatResult attack(@RequestBody AttackRequest request) {
        return combatService.attack(request.getPlayerId());
    }
}
