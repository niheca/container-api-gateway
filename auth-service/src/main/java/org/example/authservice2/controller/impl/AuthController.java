package org.example.authservice2.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.authservice2.commons.dtos.TokenResponse;
import org.example.authservice2.commons.dtos.UserRequest;
import org.example.authservice2.controller.AuthApi;
import org.example.authservice2.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Slf4j
@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> createUser(UserRequest userRequest) {
        try{
            TokenResponse tokenResponse = authService.createUser(userRequest);
            return ResponseEntity.ok(tokenResponse);
        }catch (RuntimeException e) {
            log.error("Error creating user");
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<TokenResponse> login(UserRequest userRequest) {
        try{
            authService.login(userRequest);
            return ResponseEntity.ok(authService.login(userRequest));
        }catch (RuntimeException e){
            log.error("Error login user");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId) {
        return ResponseEntity.ok(userId);
    }

}
