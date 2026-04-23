package com.easleyjs.springrpg.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String characterName;
}
