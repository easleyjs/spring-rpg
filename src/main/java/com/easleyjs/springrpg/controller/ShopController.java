package com.easleyjs.springrpg.controller;

import com.easleyjs.springrpg.dto.ShopItemResponse;
import com.easleyjs.springrpg.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
