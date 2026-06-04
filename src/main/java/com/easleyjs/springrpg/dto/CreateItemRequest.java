package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.ItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateItemRequest {
    @NotBlank String name;
    @NotBlank String description;
    @Min(0) int damageBonus;
    @Min(0) int damageMultiplier;
    @Min(0) int defenseBonus;
    @Min(0) int price;
    @NotNull ItemType itemType;
    boolean shopItem;
}
