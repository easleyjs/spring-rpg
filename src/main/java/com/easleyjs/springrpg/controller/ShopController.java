package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.ShopBuyRequest;
import com.easleyjs.springrpg.dto.ShopBuyResponse;
import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/items")
    public ResponseEntity<Page<ShopItemResponse>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(shopService.getItems(page, size));
    }

    @PostMapping("/buy")
    public ShopBuyResponse buyItem(@RequestBody ShopBuyRequest req) {
        return shopService.buyItem(req);
    }
}
