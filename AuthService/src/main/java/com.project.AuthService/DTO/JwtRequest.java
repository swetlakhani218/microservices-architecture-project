package com.project.AuthService.DTO;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}