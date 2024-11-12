package org.example.authservice2.services;

import org.example.authservice2.commons.dtos.TokenResponse;
import org.example.authservice2.commons.dtos.UserRequest;

public interface AuthService {

    TokenResponse createUser(UserRequest userRequest);
    TokenResponse login(UserRequest userRequest);

}
