package com.example.springsecurity.service;

import com.example.springsecurity.entity.UserEntity;
import com.example.springsecurity.enumeration.RoleEnum;
import com.example.springsecurity.model.AuthenticationRequest;
import com.example.springsecurity.model.AuthenticationResponse;
import com.example.springsecurity.model.RegistrationRequest;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    public AuthenticationResponse register(RegistrationRequest registrationRequest){
        var userEntity = new UserEntity(0,
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                RoleEnum.USER,
                UUID.randomUUID().toString(),
                false
                );

        var usernameAlreadyExist = userRepository.findByUsername(userEntity.getUsername());
        var emailAlreadyExist = userRepository.findByEmail(userEntity.getEmail());

        if(usernameAlreadyExist.isPresent()){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if(emailAlreadyExist.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }
        var token = jwtService.generateToken(userEntity);
        var message = String.format(
                "press on link to activate your account http://localhost:9090/api/auth/activate/%s", userEntity.getActivationCode());
        mailService.sendMail(userEntity.getEmail(), "Verification email", message);
        userRepository.save(userEntity);
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

    public UserEntity activateUser(String code) {
        UserEntity user = userRepository.findByActivationCode(code).orElseThrow(() -> new IllegalStateException("Not activated"));
        user.setIsActivated(true);
        return userRepository.save(user);
    }
}
