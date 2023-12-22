package com.gmail.apachdima.ptt.user.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {

    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateAccessToken(UserDetails userDetails);
    String generateAccessToken(Map<String, Object> Claims, UserDetails userDetails);
    long getAccessTokenExpirationTime();
    void invalidateToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
