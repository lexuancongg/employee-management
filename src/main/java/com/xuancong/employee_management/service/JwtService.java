package com.xuancong.employee_management.service;

import com.xuancong.employee_management.exception.UnauthorizedException;
import com.xuancong.employee_management.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.signerKey}")
    private  String SECRET;
    private static final long ACCESS_TOKEN_EXP_MS = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXP_MS = 7L * 24 * 60 * 60 * 1000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_MS))
                .signWith(getSignKey())
                .compact();
    }
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", "refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_MS))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public long getAccessTokenExpirationSeconds() {
        return ACCESS_TOKEN_EXP_MS / 1000;
    }

    public String extractType(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("type", String.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }
    public String extractJti(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getId(); // chính là jti
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }



}
