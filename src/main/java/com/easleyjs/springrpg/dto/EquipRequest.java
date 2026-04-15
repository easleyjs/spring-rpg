package com.easleyjs.springrpg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class EquipRequest {
    long itemId;
    long playerId;
}
