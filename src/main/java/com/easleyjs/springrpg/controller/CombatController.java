package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.AttackRequest;
import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.dto.EncounterStartRequest;
import com.easleyjs.springrpg.dto.EncounterStartResponse;
import com.easleyjs.springrpg.entity.Encounter;
import com.easleyjs.springrpg.entity.User;
import com.easleyjs.springrpg.service.CombatService;
import com.easleyjs.springrpg.service.EncounterService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public EncounterStartResponse create() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Encounter enc = encounterService.create(user.getId());

        return new EncounterStartResponse(
                enc.getId(),
                enc.getPlayerHp(),
                enc.getMonsterHp(),
                enc.getStatus()
        );
    }

    @PostMapping("/attack")
    public CombatResult attack(@RequestBody AttackRequest request) {
        return combatService.attack(request.getEncounterId());
    }

    @GetMapping("/{id}")
    public Encounter getEncounter(@PathVariable long id) {
        return encounterService.getEncounter(id);
    }

    @GetMapping()
    public List<Encounter> getAllEncounters() {
        return encounterService.getAllEncounters();
    }
}
