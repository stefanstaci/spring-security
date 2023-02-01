package com.example.springsecurity.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String Password;
}
