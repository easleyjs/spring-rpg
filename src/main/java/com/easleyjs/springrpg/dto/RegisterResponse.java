package com.easleyjs.springrpg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterResponse {
    private String username;
    private String password;
    private String characterName;
}
