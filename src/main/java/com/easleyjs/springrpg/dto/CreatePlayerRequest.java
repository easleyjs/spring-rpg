package com.easleyjs.springrpg.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class CreatePlayerRequest {
    @NotBlank String name;
}
