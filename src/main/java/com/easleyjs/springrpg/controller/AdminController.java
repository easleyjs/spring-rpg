package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.CreateItemRequest;
import com.easleyjs.springrpg.dto.CreateItemResponse;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ItemService itemService;

    public AdminController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("admin route works");
    }

    @PostMapping("/items")
    public CreateItemResponse addItem(@RequestBody CreateItemRequest item) {
        return itemService.createItem(item);
    }

}