package com.easleyjs.springrpg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    @NotBlank private String username;
    @NotBlank
    @Size(min = 8, message = "Password must be 8 characters or greater.")
    private String password;
}
