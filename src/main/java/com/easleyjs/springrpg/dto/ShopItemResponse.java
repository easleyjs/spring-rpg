package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopItemResponse {
    private String itemName;
    private String itemDescription;
    private ItemType itemType;
    private long itemPrice;
}
