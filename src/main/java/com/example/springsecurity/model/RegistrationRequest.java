package com.example.springsecurity.model;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String email;
    private String Password;
}
