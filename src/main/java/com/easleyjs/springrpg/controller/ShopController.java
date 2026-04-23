package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.ShopBuyRequest;
import com.easleyjs.springrpg.dto.ShopBuyResponse;
import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.service.ShopService;
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
    public List<ShopItemResponse> getItems() {
        return shopService.getItems();
    }

    @PostMapping("/buy")
    public ShopBuyResponse buyItem(@RequestBody ShopBuyRequest req) {
        return shopService.buyItem(req);
    }
}
