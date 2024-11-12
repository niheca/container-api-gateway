package org.example.authservice2.controller;

import jakarta.validation.Valid;
import org.example.authservice2.commons.constants.ApiPathConstants;
import org.example.authservice2.commons.dtos.TokenResponse;
import org.example.authservice2.commons.dtos.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {

    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRequest userRequest);
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid UserRequest userRequest);

}
