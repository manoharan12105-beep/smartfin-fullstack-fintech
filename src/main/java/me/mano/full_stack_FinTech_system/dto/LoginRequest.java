package me.mano.full_stack_FinTech_system.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
