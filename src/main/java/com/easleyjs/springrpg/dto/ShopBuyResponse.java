package com.easleyjs.springrpg.dto;

import lombok.Data;

@Data
public class ShopBuyResponse {
    String itemName;
    int quantity;
    int remainingGold;

    public ShopBuyResponse(String itemName, int quantity, int remainingGold) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.remainingGold = remainingGold;
    }

}
