package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.CreateItemRequest;
import com.easleyjs.springrpg.dto.CreateItemResponse;
import com.easleyjs.springrpg.dto.ItemDetailResponse;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/items")
    public List<ItemDetailResponse> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return itemService.getAllItems(page, size).getContent();
    }

    @PostMapping("/items")
    public CreateItemResponse addItem(
            @Valid @RequestBody CreateItemRequest item) {
        return itemService.createItem(item);
    }

}