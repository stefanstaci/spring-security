package com.example.springsecurity.service;

import com.example.springsecurity.entity.UserEntity;
import com.example.springsecurity.enumeration.RoleEnum;
import com.example.springsecurity.model.AuthenticationRequest;
import com.example.springsecurity.model.AuthenticationResponse;
import com.example.springsecurity.model.RegistrationRequest;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        var userEntity = new UserEntity(0, registrationRequest.getUsername(), registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()), RoleEnum.USER);
        userRepository.save(userEntity);
        var token = jwtService.generateToken(userEntity);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        var userEntiy = this.userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();

        return new AuthenticationResponse(jwtService.generateToken(userEntiy));
    }
}
