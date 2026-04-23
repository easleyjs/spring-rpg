package com.easleyjs.springrpg.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShopBuyRequest {
    private long itemId;
    private long playerId;
}
