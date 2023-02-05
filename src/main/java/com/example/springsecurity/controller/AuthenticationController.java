package com.example.springsecurity.controller;

import com.example.springsecurity.entity.UserEntity;
import com.example.springsecurity.model.AuthenticationRequest;
import com.example.springsecurity.model.AuthenticationResponse;
import com.example.springsecurity.model.RegistrationRequest;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/authentication")
    public AuthenticationResponse authentication(@RequestBody AuthenticationRequest authenticationRequest) {
        return userService.authenticate(authenticationRequest);
    }

    @PostMapping("/registration")
    public AuthenticationResponse registration(@RequestBody RegistrationRequest registrationRequest) {
        return userService.register(registrationRequest);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<UserEntity> activate(@PathVariable String code) {
        return new ResponseEntity<UserEntity>(userService.activateUser(code), HttpStatus.CREATED);
    }
}
