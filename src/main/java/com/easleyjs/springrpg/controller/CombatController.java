package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.AttackRequest;
import com.easleyjs.springrpg.dto.CombatResult;
import com.easleyjs.springrpg.service.CombatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/combat")
public class CombatController {
    private final CombatService service;

    public CombatController(CombatService service) {
        this.service = service;
    }

    @PostMapping("/attack")
    public CombatResult attack(@RequestBody AttackRequest request) {
        return service.attack(request.getPlayerId());
    }
}
