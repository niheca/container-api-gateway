package org.example.authservice2.services.impl;

import org.example.authservice2.commons.dtos.TokenResponse;
import org.example.authservice2.commons.dtos.UserRequest;
import org.example.authservice2.commons.entities.UserModel;
import org.example.authservice2.commons.repositories.UserRepository;
import org.example.authservice2.services.AuthService;
import org.example.authservice2.services.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest).map(this::mapToEntity)
                .filter(user -> userRepository.findByEmail(userRequest.getEmail()).isEmpty())
                .map(userRepository::save)
                .map(userCreated ->jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("User creation failed"));
    }

    @Override
    public TokenResponse login(UserRequest userRequest) {
            Long idUser = Long.valueOf(userRepository.findByEmail(userRequest.getEmail()).get().getId());
            return Optional.of(userRequest).map(this::mapToEntity)
                    .flatMap(user -> userRepository.findByEmail(user.getEmail()))
                    .filter(userDb -> passwordEncoder.matches(userRequest.getPassword(), userDb.getPassword()))
                    .map(user -> jwtService.generateToken(idUser))
                    .orElseThrow(() -> new RuntimeException("User login failed"));
    }

    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role("USER")
                .build();
    }

}
