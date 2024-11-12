package org.example.authservice2.services;

import io.jsonwebtoken.Claims;
import org.example.authservice2.commons.dtos.TokenResponse;
import org.springframework.stereotype.Service;


public interface JwtService {
    TokenResponse generateToken(Long userId);
    Claims getClaims(String token);
    boolean isExpired(String token);
    Integer extractUserId(String token);
}
